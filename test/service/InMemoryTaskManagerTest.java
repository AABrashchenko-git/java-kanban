package service;

import org.junit.jupiter.api.*;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.InMemoryTaskManager;
import ru.practicum.taskTracker.service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager init() {
        return new InMemoryTaskManager();
    }

    @BeforeEach
    void beforeEach() {
        manager = init();
        task1 = new Task("taskName", "testDescription");
        task2 = new Task("taskName2", "testDescription2");
        epic1 = new Epic("epicName", "testDescription");
        epic2 = new Epic("epicName", "testDescription2");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subTask1 = new SubTask(epic1.getId(), "имяПодзадачи1Эпика",
                "описаниеПодзадачи1Эпика1");
        subTask2 = new SubTask(epic1.getId(), "имяПодзадачи2Эпика",
                "описаниеПодзадачи2Эпика1");
        subTask3 = new SubTask(epic1.getId(), "имяПодзадачи3Эпика",
                "описаниеПодзадачи1Эпика2");
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
    }
    
    // Старые тесты
    @Test
    void ensureNoCollisionsInTasksWithGivenAndGeneratedId() {
        //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
        task1.setId(manager.getNextId());
        task2.setId(task1.getId());

        assertEquals(task1, task2, "Задачи с одинаковым id не равны!");
    }

    @Test
    void ensureTaskIsTheSameAfterAdded() {
        // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
        Task taskFromMap = manager.getTaskById(task1.getId());
        Epic epicFromMap = manager.getEpicById(epic1.getId());
        SubTask subTaskFromMap = manager.getSubTaskById(subTask1.getId());

        // Проверяем неизменность задач
        assertEquals(taskFromMap.getId(), task1.getId(), "ID задач до и после добавления не равны");
        assertEquals(taskFromMap.getName(), task1.getName(), "Имена задач до и после добавления не равны");
        assertEquals(taskFromMap.getDescription(), task1.getDescription(), "Описания задач до и после добавления не равны");
        assertEquals(taskFromMap.getStatus(), task1.getStatus(), "Статусы задач до и после добавления не равны");
        // Проверяем неизменность эпиков
        assertEquals(epicFromMap.getId(), epic1.getId(), "ID задач до и после добавления не равны");
        assertEquals(epicFromMap.getName(), epic1.getName(), "Имена задач до и после добавления не равны");
        assertEquals(epicFromMap.getDescription(), epic1.getDescription(), "ОЛписания задач до и после добавления не равны");
        assertEquals(epicFromMap.getStatus(), epic1.getStatus(), "Статусы задач до и после добавления не равны");
        // Проверяем неизменность сабтасок
        assertEquals(subTaskFromMap.getId(), subTask1.getId(), "ID задач до и после добавления не равны");
        assertEquals(subTaskFromMap.getName(), subTask1.getName(), "Имена задач до и после добавления не равны");
        assertEquals(subTaskFromMap.getDescription(), subTask1.getDescription(), "ОЛписания задач до и после добавления не равны");
        assertEquals(subTaskFromMap.getStatus(), subTask1.getStatus(), "Статусы задач до и после добавления не равны");
        assertEquals(subTaskFromMap.getEpicId(), subTask1.getEpicId(), "ID эпика задач до и после добавления не равны");
    }

    @Test
    void ensureSubTaskAreNotContainedAnywhereAfterRemoval() {
        // Удаляемые подзадачи не должны хранить внутри себя старые id
        // + Внутри эпиков не должно оставаться неактуальных id подзадач
        // просматриваем подзадачу
        SubTask subTask = manager.getSubTaskById(subTask1.getId());
        // Проверяем, что подзадача ушла в историю и затем удаляем её
        assertTrue(manager.getHistory().contains(subTask1));
        assertEquals(1, manager.getHistory().size());
        manager.removeOneSubTaskById(subTask1.getId());
        // Проверяем, что после удаления задачи не существуют в списке подзадач
        SubTask removedSubTask = manager.getSubTaskById(subTask1.getId());
        assertNull(removedSubTask,
                "Удаленную подзадачу можно получить по id!");
        // Проверяем, что эпик больше не содержит неактуальную удаленную подзадачу
        assertEquals(0, epic1.getSubTasksIdList().size(),
                "Удаленная подзадача содержится в эпике!");
        // Проверяем, что подзадача с данным id больше не хранится в истории
        for (Task subTaskInHistory : manager.getHistory()) {
            assertNotEquals(subTask1.getId(), subTaskInHistory.getId());
        }
    }

}