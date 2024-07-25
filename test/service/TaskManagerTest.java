package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.model.Status;
import ru.practicum.taskTracker.model.SubTask;
import ru.practicum.taskTracker.model.Task;
import ru.practicum.taskTracker.service.InMemoryTaskManager;
import ru.practicum.taskTracker.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {
    T manager = init();
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;

/*   @BeforeEach
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
    }*/

    abstract T init();

    @Test
    void addTask() {
        assertNotNull(manager.getTaskById(task1.getId()), "Task not found");
        assertNotNull(manager.getTaskById(task2.getId()), "Task not found");
        assertEquals(task1, manager.getTaskById(task1.getId()), "Task are not equal");
        assertEquals(task2, manager.getTaskById(task2.getId()), "Task are not equal");
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
    void updateTasks() {
        Task testTask = new Task(task1.getId(), "updatedName", "updatedDescription", Status.DONE);
        manager.updateTask(testTask);
        assertEquals(testTask, manager.getTaskById(task1.getId()), "Task are not equal");

        Epic testEpic = new Epic(epic1.getId(), "updatedName", "updatedDescription");
        manager.updateEpic(testEpic);
        assertEquals(testEpic, manager.getEpicById(epic1.getId()), "Task are not equal");

        SubTask testSubTask = new SubTask(epic1.getId(), subTask1.getId(), "updatedName",
                "updatedDescription", Status.DONE);
        manager.updateTask(testSubTask);
        assertEquals(testSubTask, manager.getSubTaskById(subTask1.getId()), "Task are not equal");
    }

    @Test
    void getTaskById() {
        Task testTask = manager.getTaskById(task1.getId());
        Epic testEpic = manager.getEpicById(epic1.getId());
        Task testSubTask = manager.getSubTaskById(subTask1.getId());
        assertEquals(testTask, manager.getTaskById(task1.getId()), "Task are not equal");
        assertEquals(testEpic, manager.getEpicById(epic1.getId()), "Task are not equal");
        assertEquals(testSubTask, manager.getSubTaskById(subTask1.getId()), "Task are not equal");

    }

    @Test
    void getAllTasks() {
        List<Task> tasks = List.of(task1, task2);
        List<Epic> epics = List.of(epic1, epic2);
        List<SubTask> subTasks = List.of(subTask1, subTask2, subTask3);

        assertEquals(tasks, manager.getAllTasks(), "Task are not equal");
        assertEquals(epics, manager.getAllEpics(), "Task are not equal");
        assertEquals(subTasks, manager.getAllSubTasks(), "Task are not equal");
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
        assertTrue(manager.getAllTasks().contains(task1));
        assertTrue(manager.getAllEpics().contains(epic1));
        assertTrue(manager.getAllSubTasks().contains(subTask1));

        manager.removeOneTaskById(task1.getId());
        manager.removeOneSubTaskById(subTask1.getId());
        manager.removeOneEpicById(epic1.getId());
        assertFalse(manager.getAllTasks().contains(task1));
        assertFalse(manager.getAllEpics().contains(epic1));
        assertFalse(manager.getAllSubTasks().contains(subTask1));

    }

    @Test
    void getAllSubTaskOfEpic() {
        List<SubTask> epicSubTasks = List.of(subTask1, subTask2, subTask3);
        assertEquals(epicSubTasks, manager.getAllSubTaskOfEpic(epic1.getId()));

    }

    @Test
    void getNextId() {
        assertEquals(8, manager.getNextId());
    }

    @Test
    void getHistory() {
        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask1.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getEpicById(epic1.getId());
        List<Task> history = List.of(task1, subTask1, subTask2, epic1);
        assertEquals(history, manager.getHistory());
    }

    @Test
    void getPrioritizedTasks() {
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(20));
        task2.setStartTime(LocalDateTime.now().plusMinutes(15));
        subTask1.setStartTime(LocalDateTime.now().plusMinutes(30));
        subTask1.setDuration(Duration.ofMinutes(20));

    }

    @Test
    void getTaskByIdOptional() {
        Optional<Task> optional = Optional.empty();
        assertEquals(task1, manager.getTaskByIdOptional(task1.getId()).get());
        assertEquals(optional, manager.getTaskByIdOptional(999));

        assertEquals(epic1, manager.getEpicByIdOptional(epic1.getId()).get());
        assertEquals(optional, manager.getEpicByIdOptional(999));

        assertEquals(subTask1, manager.getSubTaskByIdOptional(subTask1.getId()).get());
        assertEquals(optional, manager.getSubTaskByIdOptional(999));
    }



}
