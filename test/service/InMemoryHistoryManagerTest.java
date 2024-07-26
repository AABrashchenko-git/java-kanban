package service;

import org.junit.jupiter.api.*;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private TaskManager manager;
    private Task testTask;
    private Epic testEpic;
    private SubTask testSubTask;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
        testTask = new Task("taskName", "testDescription");
        manager.addTask(testTask);
        testEpic = new Epic("epicName", "epicTestDescription");
        manager.addEpic(testEpic);
        testSubTask = new SubTask(testEpic.getId(), "subTaskName", "subTestDescription");
        manager.addSubTask(testSubTask);
    }

    // Исправляем старые тесты - создаем новые под новую функциональность
    @Test
    void ensureHistoryCapacityIsNotLimited() {
        // Создаем 15 задач и проверяем, что в списке истории могут храниться больше 10 задач
        for (int i = 0; i < 15; i++) {
            manager.addTask(new Task("тестовое имя", "тестовое описание"));
        }
        for (Task task : manager.getAllTasks()) {
            manager.getTaskById(task.getId());
        }
        assert (manager.getHistory().size() > 10);
    }

    @Test
    void ensureHistoryShouldRemoveAndReplaceOldTaskAfterUpdate() {
        // Получаем задачу:
        Task oldTask = manager.getTaskById(testTask.getId()); // ушло в историю
        // Проверяем, что в истории в действительности присутствует данная задача
        assertEquals(manager.getHistory().get(0), oldTask,
                "история не сохранила задачу после добавления!");
        // Обновим задачу
        manager.updateTask(new Task(testTask.getId(), "newTaskName",
                "newDescription", Status.DONE));
        Task updatedTask = manager.getTaskById(oldTask.getId()); // обновленная задача должна заменить старую в истории
        List<Task> historyList = manager.getHistory();
        // Проверяем, что в списке 1 элемент (обновленная задача заменила старую)
        assertEquals(1, historyList.size());
        //Проверяем, что в истории содержится именно обновленная задача
        assertEquals(manager.getHistory().get(0), updatedTask,
                "история сохранила старую задачу после обновления!");
    }

    @Test
    void ensureHistoryShouldRemoveAndReplaceOldEpicAfterUpdate() {
        // Аналогично для эпиков. Получаем эпик:
        Epic oldEpic = manager.getEpicById(testEpic.getId()); // ушел в историю
        // Проверяем, что в истории в действительности присутствует данный эпик
        assertEquals(manager.getHistory().get(0), oldEpic,
                "история не сохранила эпик после добавления!");
        // Обновим эпик
        manager.updateEpic(new Epic(testEpic.getId(), "newTaskName",
                "newDescription"));
        Epic updatedEpic = manager.getEpicById(oldEpic.getId()); // обновленный эпик должен заменить старый в истории
        List<Task> historyList = manager.getHistory();
        // Проверяем, что в списке 1 элемент (обновленный эпик заменил старый)
        assertEquals(1, historyList.size());
        //Проверяем, что в истории содержится именно обновленный эпик
        assertEquals(manager.getHistory().get(0), updatedEpic,
                "история сохранила старый эпик после обновления!");
    }

    @Test
    void ensureHistoryShouldRemoveAndReplaceOldSubTaskAfterUpdate() {
        // Аналогично для сабтасок. Получаем сабтаску:
        SubTask oldSubTask = manager.getSubTaskById(testSubTask.getId()); // подзадача ушла в историю
        // Проверяем, что в истории в действительности присутствует данная подзадача
        assertEquals(manager.getHistory().get(0), oldSubTask,
                "история не сохранила подзадачу после добавления!");
        // Обновим подзадачу
        manager.updateSubTask(new SubTask(testEpic.getId(), testSubTask.getId(), "newTaskName",
                "newDescription", Status.DONE));
        SubTask updatedSubTask = manager.getSubTaskById(oldSubTask.getId());
        List<Task> historyList = manager.getHistory();
        // Проверяем, что в списке 1 элемент (обновленная подзадача заменила старую)
        assertEquals(1, historyList.size());
        //Проверяем, что в истории содержится именно обновленная подзадача
        assertEquals(manager.getHistory().get(0), updatedSubTask,
                "история сохранила старую подзадачу после обновления!");
        // Проверим, что при вызове эпика подзадачи у нас увеличится размер истории:
        Epic oldEpic = manager.getEpicById(testEpic.getId());
        assertEquals(2, manager.getHistory().size());
    }

    // Спринт 6
    // 1. Проверяем, что история не сохраняет дубликаты задач при добавлении
    @Test
    void ensureHistoryShouldNotKeepCopyOfTask() {
        //1. Задачи
        Task sameTask1 = manager.getTaskById(testTask.getId());
        Task sameTask2 = manager.getTaskById(testTask.getId());
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void ensureHistoryShouldNotKeepCopyOfEpic() {
        // 2. Эпики
        Epic sameEpic1 = manager.getEpicById(testEpic.getId());
        Epic sameEpic2 = manager.getEpicById(testEpic.getId());
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void ensureHistoryShouldNotKeepCopyOfSubTask() {
        // 3. Подзадачи
        SubTask sameEpic1 = manager.getSubTaskById(testSubTask.getId());
        SubTask sameEpic2 = manager.getSubTaskById(testSubTask.getId());
        assertEquals(1, manager.getHistory().size());
    }

    // 2. Проверяем, что история удалает задачи после удаления их из списка
    @Test
    void ensureHistoryShouldNotContainAnyKindOfTaskAfterRemoval() {
        //  просматриваем и удаляем задачи
        SubTask subTask = manager.getSubTaskById(testSubTask.getId());
        Epic epic = manager.getEpicById(testEpic.getId());
        Task task = manager.getTaskById((testTask.getId()));
        assertEquals(3, manager.getHistory().size());
        manager.removeOneTaskById(testTask.getId());
        manager.removeOneSubTaskById(subTask.getId());
        manager.removeOneEpicById(testEpic.getId());
        // проверяем, что задачи удалились также и из истории
        assertEquals(0, manager.getHistory().size(), "Задачи остались в истории после удаления!");
        // или
        assertFalse(manager.getHistory().contains(testTask));
        assertFalse(manager.getHistory().contains(testEpic));
        assertFalse(manager.getHistory().contains(testEpic));
    }

    // 3. Проверим корректность работы созданного связного списка
    @Test
    void ensureHistoryContainsCorrectOrderWhenAddingTasks() {
        // Создаем дополнительные задачи, добаляем
        Task testTask2 = new Task("taskName2", "testDescription2");
        manager.addTask(testTask2);
        Epic testEpic2 = new Epic("epicName2", "epicTestDescription2");
        manager.addEpic(testEpic2);
        SubTask testSubTask2 = new SubTask(testEpic2.getId(), "subTaskName2", "subTestDescription2");
        manager.addSubTask(testSubTask2);
        // просматриваем
        Task task = manager.getTaskById((testTask.getId()));
        Task task2 = manager.getTaskById((testTask2.getId()));
        Epic epic = manager.getEpicById(testEpic.getId());
        Epic epic2 = manager.getEpicById(testEpic2.getId());
        SubTask subTask = manager.getSubTaskById(testSubTask.getId());
        SubTask subTask2 = manager.getSubTaskById(testSubTask2.getId());
        // Сохраняем задачи во встроенный LinkedList
        List<Task> listWithSurelyCorrectOrder = new LinkedList<>();
        listWithSurelyCorrectOrder.add(testTask);
        listWithSurelyCorrectOrder.add(testTask2);
        listWithSurelyCorrectOrder.add(testEpic);
        listWithSurelyCorrectOrder.add(testEpic2);
        listWithSurelyCorrectOrder.add(testSubTask);
        listWithSurelyCorrectOrder.add(testSubTask2);
        // Смотрим, что история сохраняет порядок просмотра
        for (int i = 0; i < manager.getHistory().size(); i++) {
            assertEquals(listWithSurelyCorrectOrder.get(i), manager.getHistory().get(i),
                    "История не сохраняет порядок просмотра!");
        }
    }

    // Тест к ТЗ-8. Других тестов не добавлял, т.к. все требуемое уже протестировано
    // Удаление из истории: начало, середина, конец
    @Test
    void ensureHistoryRemovesTasksAppropriately() {
        //  дополнительно создаем и добавляем пару задач
        Task testTask2 = new Task("имяЗадачи2", "описаниеЗадачи2",
                LocalDateTime.now(), Duration.ofMinutes(30));
        Epic testEpic2 = new Epic("имяЭпика2", "описаниеЭпика2");
        manager.addEpic(testEpic2);
        manager.addTask(testTask2);

        Task task1 = manager.getTaskById((testTask.getId()));
        Task task2 = manager.getTaskById((testTask2.getId()));
        SubTask subTask1 = manager.getSubTaskById(testSubTask.getId());
        Epic epic1 = manager.getEpicById(testEpic.getId());
        Epic epic2 = manager.getEpicById(testEpic2.getId());

        List<Task> tasks = new LinkedList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(subTask1);
        tasks.add(epic1);
        tasks.add(epic2);

        assertEquals(tasks, manager.getHistory());
        // Удаление из начала
        manager.removeOneTaskById(testTask.getId());
        tasks.remove(task1);
        assertEquals(tasks, manager.getHistory());
        // Удаление из середины
        manager.removeOneSubTaskById(testSubTask.getId());
        tasks.remove(subTask1);
        assertEquals(tasks, manager.getHistory());
        // Удаление из конца
        manager.removeOneEpicById(testEpic2.getId());
        tasks.remove(epic2);
        assertEquals(tasks, manager.getHistory());
    }

}