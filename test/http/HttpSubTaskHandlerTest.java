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
import ru.practicum.taskTracker.model.SubTask;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpSubTaskHandlerTest {
    private TaskManager manager;
    private HttpTaskServer server;
    HttpClient client;
    URI url;
    Gson gson;
    SubTask subTask1;
    SubTask subTask2;
    Epic epic1;
    Epic epic2;

    @BeforeEach
    void beforeEach() throws IOException {
        manager = Managers.getFileBackedManager();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/subtasks");
        epic1 = new Epic("Test Name", "Test Description");
        epic2 = new Epic("Test Name2", "Test Description2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subTask1 = new SubTask(epic1.getId(), "Test Name", "Test Description",
                LocalDateTime.now(), Duration.ofMinutes(5));
        subTask2 = new SubTask(epic1.getId(), "Test Name2", "Test Description2");
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
    void testHandleGetOneSubTask() throws IOException, InterruptedException {
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        url = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskFromResponse1 = gson.fromJson(response1.body(), SubTask.class);
        assertEquals(manager.getSubTaskById(3), subTaskFromResponse1);
        assertEquals(200, response1.statusCode());

        url = URI.create("http://localhost:8080/subtasks/4");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskFromResponse2 = gson.fromJson(response2.body(), SubTask.class);
        assertEquals(manager.getSubTaskById(4), subTaskFromResponse2);
        assertEquals(200, response2.statusCode());
        // Получим несуществующую задачу
        url = URI.create("http://localhost:8080/subtasks/999");
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());
        assertThrows(JsonSyntaxException.class, () ->
                gson.fromJson(response3.body(), SubTask.class));
    }

    @Test
    void testHandleAddOrUpdateSubTask() throws InterruptedException, IOException {
        // Тестируем добавление задач
        String subTaskJson1 = gson.toJson(subTask1);
        String subTaskJson2 = gson.toJson(subTask2);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson1)).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response1.statusCode());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson2)).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response2.statusCode());

        assertNotNull(manager.getAllSubTasks(), "Задачи не возвращаются");
        assertEquals(2, manager.getAllSubTasks().size(), "Некорректное количество задач");
        assertEquals("Test Name", manager.getAllSubTasks().get(0).getName(), "Некорректное имя задачи");
        assertEquals("Test Name2", manager.getAllSubTasks().get(1).getName(), "Некорректное имя задачи");

        // Тестируем обновление задач
        String subTaskJsonUpd1 = gson.toJson(new SubTask(epic1.getId(), 3, "New Test Name 1",
                "New Test Description1",
                Status.IN_PROGRESS, LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5)));
        String subTaskJsonUpd2 = gson.toJson(new SubTask(epic1.getId(), 4, "New Test Name 2",
                "New Test Description2", Status.DONE));

        HttpRequest requestUpd1 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJsonUpd1)).build();
        HttpResponse<String> responseUpd1 = client.send(requestUpd1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpd1.statusCode());

        HttpRequest requestUpd2 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJsonUpd2)).build();
        HttpResponse<String> responseUpd2 = client.send(requestUpd2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpd2.statusCode());

        assertNotNull(manager.getAllSubTasks(), "Задачи не возвращаются");
        assertEquals(2, manager.getAllSubTasks().size(), "Некорректное количество задач");
        assertEquals("New Test Name 1", manager.getAllSubTasks().get(0).getName(), "Некорректное имя задачи");
        assertEquals("New Test Name 2", manager.getAllSubTasks().get(1).getName(), "Некорректное имя задачи");

        // Добавим задачу, которая пересекается с имеющейся
        SubTask subTask3 = new SubTask(epic1.getId(), "Test Name 3", "Test Description 3",
                LocalDateTime.now().plusMinutes(11), Duration.ofMinutes(5));
        String subTaskJson3 = gson.toJson(subTask3);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson3)).build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response3.statusCode());
        assertEquals(2, manager.getAllSubTasks().size(), "Некорректное количество задач");
    }

    @Test
    void testHandleGetAllSubTasks() throws IOException, InterruptedException {
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTasksFromResponse = gson.fromJson(response1.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(manager.getAllSubTasks(), subTasksFromResponse);
        assertEquals(200, response1.statusCode());
    }

    @Test
    void testHandleRemoveOneSubTask() throws IOException, InterruptedException {
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        assertEquals(2, manager.getAllSubTasks().size());

        url = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());
        assertEquals(1, manager.getAllSubTasks().size());
        // Попробуем удалить еще раз ту же задачу
        url = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response2.statusCode());
    }

    @Test
    void testHandleRemoveAllSubTasks() throws IOException, InterruptedException {
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(manager.getAllSubTasks(), Collections.emptyList());
        assertEquals(200, response1.statusCode());
    }


}
