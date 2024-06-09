package ru.practicum.taskTracker.service;

import org.junit.jupiter.api.*;
import ru.practicum.taskTracker.model.*;

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
        // Создаем 15 задач и проверяем, что в списке истории хранится только 10
        for (int i = 0; i < 15; i++) {
            manager.addTask(new Task("тестовое имя", "тестовое описание"));
        }
        for (Task task : manager.getAllTasks()) {
            manager.getTaskById(task.getId());
        }
        assert (manager.getHistory().size() == 10);
    }

    @Test
    void ensureHistoryKeepsOldVersionOfTaskAfterUpdate() {
        // Убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных
        // Создаем задачу, получаем её, обновляем и смотрим на историю
        Task testTask = new Task("taskName", "testDescription");
        manager.addTask(testTask);
        Task oldTask = manager.getTaskById(testTask.getId());
        manager.updateTask(new Task(testTask.getId(), "newTaskName",
                "newDescription", Status.DONE));
        Task updatedTask = manager.getTaskById(oldTask.getId());
        List<Task> historyList = manager.getHistory();
        // Проверяем, что в списке 2 элемента (старая и обновленная задача)
        assertEquals(2, historyList.size());
        //Проверяем, что в истории содержатся старые данные,
        assertEquals(historyList.get(0).getName(), oldTask.getName(), "история не сохраняет старые задачи!");
        assertEquals(historyList.get(0).getDescription(), oldTask.getDescription(), "история не сохраняет старые задачи!");
        // Проверяем, что в истории содержатся также новые данные
        assertEquals(historyList.get(1).getName(), updatedTask.getName());
        assertEquals(historyList.get(1).getDescription(), updatedTask.getDescription());
    }

    @Test
    void ensureHistoryKeepsOldVersionOfEpicsAfterUpdate() {
        // аналогично для эпиков
        Epic testEpic = new Epic("epicName", "epicTestDescription");
        manager.addEpic(testEpic);
        Epic oldEpic = manager.getEpicById(testEpic.getId());
        manager.updateEpic(new Epic(testEpic.getId(), "newTaskName",
                "newDescription"));
        Epic updatedEpic = manager.getEpicById(oldEpic.getId());
        List<Task> historyList = manager.getHistory();
        assertEquals(2, historyList.size());
        assertEquals(historyList.get(0).getName(), oldEpic.getName(), "история не сохраняет старые задачи!");
        assertEquals(historyList.get(0).getDescription(), oldEpic.getDescription(), "история не сохраняет старые задачи!");
        assertEquals(historyList.get(1).getName(), updatedEpic.getName());
        assertEquals(historyList.get(1).getDescription(), updatedEpic.getDescription());
    }

    @Test
    void ensureHistoryKeepsOldVersionOfSubTaskAfterUpdate() {
        // аналогично для сабтасок, но с привязкой сабтаска к эпику
        Epic testEpic = new Epic("epicName", "epicTestDescription");
        manager.addEpic(testEpic);

        SubTask testSubTask = new SubTask(testEpic.getId(), "subTaskName", "subTestDescription");
        manager.addSubTask(testSubTask);
        SubTask oldSubTask = manager.getSubTaskById(testSubTask.getId());
        manager.updateSubTask(new SubTask(testEpic.getId(), testSubTask.getId(), "newTaskName",
                "newDescription", Status.DONE));
        SubTask updatedSubTask = manager.getSubTaskById(oldSubTask.getId());
        List<Task> historyList = manager.getHistory();
        assertEquals(2, historyList.size());
        assertEquals(historyList.get(0).getName(), oldSubTask.getName(), "история не сохраняет старые задачи!");
        assertEquals(historyList.get(0).getDescription(), oldSubTask.getDescription(), "история не сохраняет старые задачи!");
        assertEquals(historyList.get(1).getName(), updatedSubTask.getName());
        assertEquals(historyList.get(1).getDescription(), updatedSubTask.getDescription());
    }
}