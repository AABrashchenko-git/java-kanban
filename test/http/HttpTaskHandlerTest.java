package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.http.HttpTaskServer;
import ru.practicum.taskTracker.http.adapters.DurationAdapter;
import ru.practicum.taskTracker.http.adapters.EpicDeserializer;
import ru.practicum.taskTracker.http.adapters.LocalDateTimeAdapter;
import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.model.Status;
import ru.practicum.taskTracker.model.Task;
import ru.practicum.taskTracker.service.Managers;
import ru.practicum.taskTracker.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskHandlerTest {
    private TaskManager manager;
    HttpTaskServer server;
    HttpClient client;
    URI url;
    Gson gson;
    Task task1;
    Task task2;

    @BeforeEach
    void beforeEach() throws IOException {
        manager = Managers.getFileBackedManager();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks");
        // Будем работать с двумя типами задач, одна с временными метками, другая без
        // Заодно посмотрим на корректность сериализации\десериализации времени
        task1 = new Task("Test Name", "Test Description", LocalDateTime.now(), Duration.ofMinutes(5));
        task2 = new Task("Test Name2", "Test Description2");
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Epic.class, new EpicDeserializer())
                .serializeNulls().create();
    }

    @AfterEach
    void afterEach() {
        server.stop();
    }

    @Test
    void testHandleGetOneTask() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);

        url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        Task taskFromResponse1 = gson.fromJson(response1.body(), Task.class);
        assertEquals(manager.getTaskById(1), taskFromResponse1);
        assertEquals(200, response1.statusCode());

        url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Task taskFromResponse2 = gson.fromJson(response2.body(), Task.class);
        assertEquals(manager.getTaskById(2), taskFromResponse2);
        assertEquals(200, response2.statusCode());
        // Получим несуществующую задачу
        url = URI.create("http://localhost:8080/tasks/999");
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());
        assertThrows(JsonSyntaxException.class, () ->
                gson.fromJson(response3.body(), Task.class));
    }

    @Test
    void testHandleAddOrUpdateTask() throws InterruptedException, IOException {
        // Тестируем добавление задач
        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson1)).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response1.statusCode());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response2.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test Name", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Test Name2", tasksFromManager.get(1).getName(), "Некорректное имя задачи");

        // Тестируем обновление задач
        String taskJsonUpd1 = gson.toJson(new Task(1, "New Test Name 1", "New Test Description1",
                Status.IN_PROGRESS, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5)));
        String taskJsonUpd2 = gson.toJson(new Task(2, "New Test Name 2",
                "New Test Description2", Status.DONE));
        HttpRequest requestUpd1 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJsonUpd1)).build();
        HttpResponse<String> responseUpd1 = client.send(requestUpd1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpd1.statusCode());

        HttpRequest requestUpd2 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJsonUpd2)).build();
        HttpResponse<String> responseUpd2 = client.send(requestUpd2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpd2.statusCode());

        assertNotNull(manager.getAllTasks(), "Задачи не возвращаются");
        assertEquals(2, manager.getAllTasks().size(), "Некорректное количество задач");
        assertEquals("New Test Name 1", manager.getAllTasks().get(0).getName(), "Некорректное имя задачи");
        assertEquals("New Test Name 2", manager.getAllTasks().get(1).getName(), "Некорректное имя задачи");

        // Добавим задачу, которая пересекается с имеющейся
        Task task3 = new Task("Test Name 3", "Test Description 3",
                LocalDateTime.now().plusMinutes(11), Duration.ofMinutes(5));
        String taskJson3 = gson.toJson(task3);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson3)).build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response3.statusCode());
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void testHandleGetAllTasks() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromResponse = gson.fromJson(response1.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(manager.getAllTasks(), tasksFromResponse);
        assertEquals(200, response1.statusCode());
    }

    @Test
    void testHandleRemoveOneTask() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);
        assertEquals(2, manager.getAllTasks().size());

        url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());
        assertEquals(1, manager.getAllTasks().size());
        // Попробуем удалить еще раз ту же задачу
        url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response2.statusCode());
    }

    @Test
    void testHandleRemoveAllTasks() throws IOException, InterruptedException {
        manager.addTask(task1);
        manager.addTask(task2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(manager.getAllTasks(), Collections.emptyList());
        assertEquals(200, response1.statusCode());
    }

}
