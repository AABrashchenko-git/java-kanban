package service;

import org.junit.jupiter.api.*;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void ensureHistoryContainsOnlyTenTasks() {
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
        // Пишем новый тест под новую функциональность. Добавляем задачу:
        Task testTask = new Task("taskName", "testDescription");
        manager.addTask(testTask);
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
        // Аналогично для эпиков. Добавляем эпик:
        Epic testEpic = new Epic("epicName", "epicTestDescription");
        manager.addEpic(testEpic);
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
        // Аналогично для сабтасок. Добавляем эпик и сабтаску:
        Epic testEpic = new Epic("epicName", "epicTestDescription");
        manager.addEpic(testEpic);
        SubTask testSubTask = new SubTask(testEpic.getId(), "subTaskName", "subTestDescription");
        manager.addSubTask(testSubTask);
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
        System.out.println(testEpic.getId());
        System.out.println(testSubTask.getId());
        // Проверим, что при вызове эпика подзадачи у нас увеличится размер истории:
        Epic oldEpic = manager.getEpicById(testEpic.getId());
        assertEquals(2, manager.getHistory().size());
    }

}