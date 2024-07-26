package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.InMemoryTaskManager;

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

}