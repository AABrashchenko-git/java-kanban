package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    InMemoryTaskManager manager;
    Epic testEpic;
    Epic testEpic2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
        testEpic = new Epic(1, "epicName", "testDescription");
        testEpic2 = new Epic(1, "epicName", "testDescription2");
        subTask1 = new SubTask(testEpic.getId(), 2, "имяПодзадачи1Эпика",
                "описаниеПодзадачи1Эпика1", Status.NEW);
        subTask2 = new SubTask(testEpic.getId(), 3, "имяПодзадачи2Эпика",
                "описаниеПодзадачи2Эпика1", Status.NEW);
        subTask3 = new SubTask(testEpic.getId(), 4, "имяПодзадачи3Эпика",
                "описаниеПодзадачи1Эпика2", Status.NEW);
        manager.addEpic(testEpic);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
    }

    @Test
    void showTwoEpicsWithOneIdAreEqual() {
        // проверьте, что экземпляры класса Epic равны друг другу, если равен их id
        assertEquals(testEpic, testEpic2, "Epics are not equal");
    }

    @Test
    void showImpossibilityOfAddingEpicAsSubTask() {
        // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task testEpic = new Epic(1, "epicName", "testDescription");
        manager.addEpic((Epic) testEpic);
        assertThrows(ClassCastException.class, () ->
                manager.addSubTask((SubTask) testEpic)
        );
    }

    @Test
    void showEpicChangesStatusAlongWithItsSubTasks() {
        // Все подзадачи со статусом NEW
        assertEquals(Status.NEW, testEpic.getStatus(), "Epic Status is invalid");
        // Все подзадачи со статусом DONE
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask1.getId(), subTask1.getName(),
                subTask1.getDescription(), Status.DONE));
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask2.getId(), subTask2.getName(),
                subTask2.getDescription(), Status.DONE));
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask3.getId(), subTask3.getName(),
                subTask3.getDescription(), Status.DONE));
        assertEquals(Status.DONE, testEpic.getStatus(), "Epic Status is invalid");
        // Подзадачи со статусами NEW и DONE
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask1.getId(), subTask1.getName(),
                subTask1.getDescription(), Status.DONE));
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask2.getId(), subTask2.getName(),
                subTask2.getDescription(), Status.NEW));
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask3.getId(), subTask3.getName(),
                subTask3.getDescription(), Status.DONE));
        assertEquals(Status.IN_PROGRESS, testEpic.getStatus(), "Epic Status is invalid");
        // Подзадачи со статусом IN_PROGRESS
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask1.getId(), subTask1.getName(),
                subTask1.getDescription(), Status.IN_PROGRESS));
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask2.getId(), subTask2.getName(),
                subTask2.getDescription(), Status.IN_PROGRESS));
        manager.updateSubTask(new SubTask(testEpic.getId(), subTask3.getId(), subTask3.getName(),
                subTask3.getDescription(), Status.IN_PROGRESS));
        assertEquals(Status.IN_PROGRESS, testEpic.getStatus(), "Epic Status is invalid");
    }
}