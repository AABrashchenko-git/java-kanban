package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerInputException;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file;

    @Override
    FileBackedTaskManager init() {
        return new FileBackedTaskManager(file);
    }

    @BeforeEach
    void beforeEach() {
        try {
            file = File.createTempFile("tasks", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        manager = init();

        task1 = new Task("taskName", "testDescription", LocalDateTime.now(), Duration.ofMinutes(20));
        task2 = new Task("taskName2", "testDescription2");
        epic1 = new Epic("epicName", "testDescription");
        epic2 = new Epic("epicName", "testDescription2");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subTask1 = new SubTask(epic1.getId(), "имяПодзадачи1Эпика",
                "описаниеПодзадачи1Эпика1", LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(20));
        subTask2 = new SubTask(epic1.getId(), "имяПодзадачи2Эпика",
                "описаниеПодзадачи2Эпика1", LocalDateTime.now().plusMinutes(51), Duration.ofMinutes(40));
        subTask3 = new SubTask(epic1.getId(), "имяПодзадачи3Эпика",
                "описаниеПодзадачи1Эпика2");
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
    }

    // Проверить сохранение и загрузку пустого файла
    @Test
    void ensureManagerSavesAndLoadsEmptyListOfTasks() {
        manager = init();
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
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubTaskById(subTask1.getId());

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(newManager.getAllTasks(), manager.getAllTasks());
        assertEquals(newManager.getAllEpics(), manager.getAllEpics());
        assertEquals(newManager.getAllSubTasks(), manager.getAllSubTasks());
        assertEquals(newManager.getHistory(), manager.getHistory());
    }

    @Test
    public void testException() {
        assertThrows(FileBackedTaskManagerInputException.class, () -> {
            File file1 = new File("nonExistingFile.txt");
            manager = FileBackedTaskManager.loadFromFile(file1);
        }, "Файл не найден!");

        assertDoesNotThrow(() -> {
            manager = FileBackedTaskManager.loadFromFile(file);
        });
    }

}
