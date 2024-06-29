package model;

import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.model.*;
import ru.practicum.taskTracker.service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void showTwoEpicsWithOneIdAreEqual() {
        // проверьте, что экземпляры класса Epic равны друг другу, если равен их id
        Epic testEpic = new Epic(1, "epicName", "testDescription");
        Epic testEpic2 = new Epic(1, "epicName", "testDescription2");
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