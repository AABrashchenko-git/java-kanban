package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.model.Status;
import ru.practicum.taskTracker.model.SubTask;
import ru.practicum.taskTracker.model.Task;
import ru.practicum.taskTracker.service.InMemoryTaskManager;
import ru.practicum.taskTracker.service.TaskManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;

    @BeforeEach
    void beforeEach() {
        manager = init();
        epic1 = new Epic(1, "epicName", "testDescription");
        epic2 = new Epic(1, "epicName", "testDescription2");
        subTask1 = new SubTask(epic1.getId(), 2, "имяПодзадачи1Эпика",
                "описаниеПодзадачи1Эпика1", Status.NEW);
        subTask2 = new SubTask(epic1.getId(), 3, "имяПодзадачи2Эпика",
                "описаниеПодзадачи2Эпика1", Status.NEW);
        subTask3 = new SubTask(epic1.getId(), 4, "имяПодзадачи3Эпика",
                "описаниеПодзадачи1Эпика2", Status.NEW);
        manager.addEpic(epic1);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
    }

    abstract T init();

    @Test
    void addTask() {
        assertNotNull(manager.getTaskById(task1.getId()), "Task not found");
        assertNotNull(manager.getTaskById(task2.getId()), "Task not found");
        assertEquals(task1, manager.getTaskById(task1.getId()), "Task are not equal");
        assertEquals(task1, manager.getTaskById(task2.getId()), "Task are not equal");
        assertNotNull(manager.getAllTasks(), "Задачи не возвращаются.");
    }

    @Test
    void addEpicAndSubTask() {
        // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
        // Эпики
        assertNotNull(manager.getEpicById(epic1.getId()), "Epic not found");
        assertNotNull(manager.getEpicById(epic2.getId()), "Epic not found");
        assertEquals(epic1, manager.getEpicById(epic1.getId()), "Epics are not equal");
        assertEquals(epic2, manager.getEpicById(epic2.getId()), "Epics are not equal");
        assertNotNull(manager.getAllEpics(), "Задачи не возвращаются.");

        // Сабтаски
        SubTask testSubTask = new SubTask(epic1.getId(), "taskName", "testDescription");
        SubTask testSubTask2 = new SubTask(epic2.getId(), "taskName2", "testDescription2");

        manager.addSubTask(testSubTask);
        manager.addSubTask(testSubTask2);

        assertNotNull(manager.getSubTaskById(testSubTask.getId()), "Task not found");
        assertNotNull(manager.getSubTaskById(testSubTask2.getId()), "Task not found");
        assertEquals(testSubTask, manager.getSubTaskById(testSubTask.getId()), "Task are not equal");
        assertEquals(testSubTask2, manager.getSubTaskById(testSubTask2.getId()), "Task are not equal");
        assertNotNull(manager.getAllTasks(), "подзадачи не возвращаются.");
    }

    @Test
    void updateTasks(Task task) {
        SubTask testSubTask = new SubTask(epic1.getId(), "taskName", "testDescription");
        //TODO проверяем что обновилась -> для всех типов сделать
    }

    @Test
    void getTaskById() {
        Task testTask = manager.getTaskById(task1.getId());
        Epic testEpic = manager.getEpicById(epic1.getId());
        Task testSubTask = manager.getSubTaskById(subTask1.getId());

    }

    @Test
    void getAllTasks() {
        List<Task> tasks = manager.getAllTasks();
        //////////
    }

    @Test
    void removeTasks() {
        manager.removeTasks();
        manager.removeEpics();
        manager.removeSubTasks();
        assertEquals(Collections.emptyList(), manager.getAllTasks());
        assertEquals(Collections.emptyList(), manager.getAllEpics());
        assertEquals(Collections.emptyList(), manager.getAllSubTasks());
        assertEquals(Collections.emptyList(), manager.getHistory());
    }

    @Test
    void removeOneTaskById() {

    }

    @Test
    void getAllSubTaskOfEpic(int epicId) {

    }

    @Test
    void getNextId() {

    }

    @Test
    void getHistory() {

    }

    @Test
    void getPrioritizedTasks() {

    }

    @Test
    void getTaskByIdOptional(int taskId) {

    }

    @Test
    void getEpicByIdOptional(int epicId) {

    }

    @Test
    void getSubTaskByIdOptional(int subTaskId) {

    }


}
