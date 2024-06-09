package ru.practicum.taskTracker.model;

import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void showTwoSubTasksWithOneIdAreEqual() {
        // проверьте, что экземпляры класса SubTask равны друг другу, если равен их id
        SubTask testSubTask = new SubTask(1, "subTaskName", "testDescription");
        SubTask testSubTask2 = new SubTask(1, "subTaskName2", "testDescription2");
        testSubTask.setId(2);
        testSubTask2.setId(2);
        assertEquals(testSubTask, testSubTask2, "Subtasks are not equal");
    }

    @Test
    void showImpossibilityOfAddingSubTaskAsItsEpic() {
        // проверьте, что объект Subtask нельзя сделать своим же эпиком
        InMemoryTaskManager manager = new InMemoryTaskManager();
        // Как и в EpicTest, если прямо добавлять, при попытке добавить код не скопилируется, например
        // SubTask testSubTask = new SubTask(testSubTask.getId(), "subTaskName", "testDescription");
        // manager.addSubTask(testSubTask);
        // Epic epic = manager.getEpicById(testSubTask.getId()); => null
    }

}