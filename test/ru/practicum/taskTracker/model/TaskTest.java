package ru.practicum.taskTracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void showTwoTasksWithOneIdAreEqual() {
        // проверьте, что экземпляры класса Task равны друг другу, если равен их id
        Task testTask = new Task("taskName", "testDescription");
        Task testTask2 = new Task("taskName2", "testDescription2");
        testTask.setId(1);
        testTask2.setId(1);
        assertEquals(testTask, testTask2, "Tasks are not equal");
    }
}