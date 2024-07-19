package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    FileBackedTaskManager manager;
    private Task testTask;
    private Task testTask2;
    private Epic testEpic;
    private Epic testEpic2;
    private SubTask testSubTask;
    File file;

    @BeforeEach
    void getNewTaskManager() {
        try {
            file = File.createTempFile("tasks", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        manager = new FileBackedTaskManager(file);
        testTask = new Task("taskName", "testDescription");
        testTask2 = new Task("taskName2", "testDescription2");
        testEpic = new Epic("taskName", "testDescription");
        testEpic2 = new Epic("taskName2", "testDescription2");
        testSubTask = new SubTask(testEpic.getId(), "taskName", "testDescription");

    }

    // Проверить сохранение и загрузку пустого файла
    @Test
    void ensureManagerSavesAndLoadsEmptyListOfTasks() {
        manager.save();
        manager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(Collections.emptyList(), manager.getAllTasks());
        assertEquals(Collections.emptyList(), manager.getAllEpics());
        assertEquals(Collections.emptyList(), manager.getAllSubTasks());
        assertEquals(Collections.emptyList(), manager.getHistory());
    }

    // Проверить сохранение нескольких задач;
    // Проверить загрузку нескольких задач
    @Test
    void ensureManagerSavesAndLoadsFilledListOfTasks() {
        manager.addTask(testTask);
        manager.addTask(testTask2);
        manager.addEpic(testEpic);
        manager.addEpic(testEpic2);
        manager.addSubTask(testSubTask);

        manager.getTaskById(testTask.getId());
        manager.getTaskById(testTask2.getId());
        manager.getEpicById(testEpic.getId());
        manager.getEpicById(testEpic2.getId());
        manager.getSubTaskById(testSubTask.getId());

        manager.save();
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(newManager.getAllTasks(), manager.getAllTasks());
        assertEquals(newManager.getAllEpics(), manager.getAllEpics());
        assertEquals(newManager.getAllSubTasks(), manager.getAllSubTasks());
        assertEquals(newManager.getHistory(), manager.getHistory());
    }

}
