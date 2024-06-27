package model;

import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.model.Status;
import ru.practicum.taskTracker.model.Task;

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

    // SPRINT 6
    // С помощью сеттеров экземпляры задач позволяют изменить любое своё поле,
    // но это может повлиять на данные внутри менеджера.
    // Протестируйте эти кейсы и подумайте над возможными вариантами решения проблемы.
    @Test
    void ensureSettersShouldNotSetInvalidValues() {
        //Подправил тела сеттеров в Task, добавил валидацию значений
        // В классе SubTask только один сеттер, который используется только внутри класса, поэтому сделал его приватным
        Task testTask = new Task("taskName", "testDescription");
        testTask.setId(1);
        // проверяем сеттер id
        testTask.setId(-999);
        assertNotEquals(-999, testTask.getId(), "Задается некорректное значение id!");
        // проверяем сеттер имени
        testTask.setName(null);
        assertNotNull(testTask.getName(), "Задается некорректное значение имени!");
        assertEquals("taskName", testTask.getName());
        // проверяем сеттер описания
        testTask.setDescription(null);
        assertNotNull(testTask.getDescription(), "Задается некорректное описания задачи!");
        assertEquals("testDescription", testTask.getDescription());
        // проверяем сеттер статуса
        testTask.setStatus(null);
        assertNotNull(testTask.getStatus(), "Задается некорректный статус задачи!");
        assertEquals(Status.NEW, testTask.getStatus());
    }
}