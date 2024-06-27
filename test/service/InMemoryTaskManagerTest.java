package service;

import org.junit.jupiter.api.*;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.InMemoryTaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager manager;

    @BeforeEach
    void getNewTaskManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void addTask() {
        // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
        // задачи
        Task testTask = new Task("taskName", "testDescription");
        Task testTask2 = new Task("taskName2", "testDescription2");

        manager.addTask(testTask);
        manager.addTask(testTask2);

        assertNotNull(manager.getTaskById(testTask.getId()), "Task not found");
        assertNotNull(manager.getTaskById(testTask2.getId()), "Task not found");
        assertEquals(testTask, manager.getTaskById(testTask.getId()), "Task are not equal");
        assertEquals(testTask2, manager.getTaskById(testTask2.getId()), "Task are not equal");
        assertNotNull(manager.getAllTasks(), "Задачи не возвращаются.");
    }

    @Test
    void addEpicAndSubTask() {
        // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
        // Эпики
        Epic testEpic = new Epic("taskName", "testDescription");
        Epic testEpic2 = new Epic("taskName2", "testDescription2");

        manager.addEpic(testEpic);
        manager.addEpic(testEpic2);

        assertNotNull(manager.getEpicById(testEpic.getId()), "Epic not found");
        assertNotNull(manager.getEpicById(testEpic2.getId()), "Epic not found");
        assertEquals(testEpic, manager.getEpicById(testEpic.getId()), "Epics are not equal");
        assertEquals(testEpic2, manager.getEpicById(testEpic2.getId()), "Epics are not equal");
        assertNotNull(manager.getAllEpics(), "Задачи не возвращаются.");

        // Сабтаски
        SubTask testSubTask = new SubTask(testEpic.getId(), "taskName", "testDescription");
        SubTask testSubTask2 = new SubTask(testEpic2.getId(), "taskName2", "testDescription2");

        manager.addSubTask(testSubTask);
        manager.addSubTask(testSubTask2);

        assertNotNull(manager.getSubTaskById(testSubTask.getId()), "Task not found");
        assertNotNull(manager.getSubTaskById(testSubTask2.getId()), "Task not found");
        assertEquals(testSubTask, manager.getSubTaskById(testSubTask.getId()), "Task are not equal");
        assertEquals(testSubTask2, manager.getSubTaskById(testSubTask2.getId()), "Task are not equal");
        assertNotNull(manager.getAllTasks(), "подзадачи не возвращаются.");

    }

    @Test
    void ensureNoCollisionsInTasksWithGivenAndGeneratedId() {
        //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
        Task testTask1 = new Task("taskName1", "testDescription1");
        Task testTask2 = new Task("taskName2", "testDescription2");

        testTask1.setId(manager.getNextId());
        testTask2.setId(testTask1.getId());

        assertEquals(testTask1, testTask2, "Задачи с одинаковым id не равны!");
    }

    @Test
    void ensureTaskIsTheSameAfterAdded() {
        // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
        Task testTask = new Task("taskName", "testDescription");
        Epic testEpic = new Epic("taskName", "testDescription");
        SubTask testSubTask = new SubTask(testEpic.getId(), "taskName", "testDescription");

        manager.addTask(testTask);
        manager.addEpic(testEpic);
        manager.addSubTask(testSubTask);

        Task taskFromMap = manager.getTaskById(testTask.getId());
        Epic epicFromMap = manager.getEpicById(testEpic.getId());
        SubTask subTaskFromMap = manager.getSubTaskById(testSubTask.getId());

        // Проверяем неизменность задач
        assertEquals(taskFromMap.getId(), testTask.getId(), "ID задач до и после добавления не равны");
        assertEquals(taskFromMap.getName(), testTask.getName(), "Имена задач до и после добавления не равны");
        assertEquals(taskFromMap.getDescription(), testTask.getDescription(), "Описания задач до и после добавления не равны");
        assertEquals(taskFromMap.getStatus(), testTask.getStatus(), "Статусы задач до и после добавления не равны");
        // Проверяем неизменность эпиков
        assertEquals(epicFromMap.getId(), testEpic.getId(), "ID задач до и после добавления не равны");
        assertEquals(epicFromMap.getName(), testEpic.getName(), "Имена задач до и после добавления не равны");
        assertEquals(epicFromMap.getDescription(), testEpic.getDescription(), "ОЛписания задач до и после добавления не равны");
        assertEquals(epicFromMap.getStatus(), testEpic.getStatus(), "Статусы задач до и после добавления не равны");
        // Проверяем неизменность сабтасок
        assertEquals(subTaskFromMap.getId(), testSubTask.getId(), "ID задач до и после добавления не равны");
        assertEquals(subTaskFromMap.getName(), testSubTask.getName(), "Имена задач до и после добавления не равны");
        assertEquals(subTaskFromMap.getDescription(), testSubTask.getDescription(), "ОЛписания задач до и после добавления не равны");
        assertEquals(subTaskFromMap.getStatus(), testSubTask.getStatus(), "Статусы задач до и после добавления не равны");
        assertEquals(subTaskFromMap.getEpicId(), testSubTask.getEpicId(), "ID эпика задач до и после добавления не равны");
    }

    // SPRINT 6
    // Удаляемые подзадачи не должны хранить внутри себя старые id
    // + Внутри эпиков не должно оставаться неактуальных id подзадач
    @Test
    void ensureSubTaskAreNotContainedAnywhereAfterRemoval() {
        // Создаем подзадачу, добаляем, просматриваем
        Epic testEpic = new Epic("epicName", "epicTestDescription");
        manager.addEpic(testEpic);
        SubTask testSubTask = new SubTask(testEpic.getId(), "subTaskName", "subTestDescription");
        manager.addSubTask(testSubTask);
        SubTask subTask = manager.getSubTaskById(testSubTask.getId());
        // Проверяем, что подзадача ушла в историю и затем удаляем её
        assertTrue(manager.getHistory().contains(testSubTask));
        assertEquals(1, manager.getHistory().size());
        manager.removeOneSubTaskById(testSubTask.getId());
        // Проверяем, что после удаления задачи не существуют в списке подзадач
        SubTask removedSubTask = manager.getSubTaskById(testSubTask.getId());
        assertNull(removedSubTask,
                "Удаленную подзадачу можно получить по id!");
        // Проверяем, что эпик больше не содержит неактуальную удаленную подзадачу
        assertEquals(0, testEpic.getSubTasksList().size(),
                "Удаленная подзадача содержится в эпике!");
        // Проверяем, что подзадача с данным id больше не хранится в истории
        for (Task subTaskInHistory : manager.getHistory()) {
            assertNotEquals(testSubTask.getId(), subTaskInHistory.getId());
        }
    }

}