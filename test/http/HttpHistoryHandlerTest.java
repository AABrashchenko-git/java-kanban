package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.http.HttpTaskServer;
import ru.practicum.taskTracker.http.adapters.DurationAdapter;
import ru.practicum.taskTracker.http.adapters.EpicDeserializer;
import ru.practicum.taskTracker.http.adapters.LocalDateTimeAdapter;
import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.model.SubTask;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpHistoryHandlerTest {
    private TaskManager manager;
    HttpTaskServer server;
    HttpClient client;
    URI url;
    Gson gson;
    Task task1;
    Task task2;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    Epic epic1;
    Epic epic2;

    @BeforeEach
    void beforeEach() throws IOException {
        manager = Managers.getFileBackedManager();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/history");
        task1 = new Task("Test Name", "Test Description",
                LocalDateTime.now(), Duration.ofMinutes(5));
        task2 = new Task("Test Name2", "Test Description2");
        epic1 = new Epic("Test Name", "Test Description");
        epic2 = new Epic("Test Name2", "Test Description2");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        subTask1 = new SubTask(epic1.getId(), "Test Name", "Test Description",
                LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(5));
        subTask2 = new SubTask(epic1.getId(), "Test Name2", "Test Description2");
        subTask3 = new SubTask(epic2.getId(), "Test Name", "Test Description",
                LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(10));
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
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
    void testHandleHistory() throws IOException, InterruptedException {
        // Получим несколько задач
        getTaskByIdWithHttpRequest(2);
        getTaskByIdWithHttpRequest(1);
        getSubTaskByIdWithHttpRequest(7);
        getSubTaskByIdWithHttpRequest(5);
        getSubTaskByIdWithHttpRequest(6);
        getEpicByIdWithHttpRequest(3);
        getEpicByIdWithHttpRequest(4);
        getTaskByIdWithHttpRequest(2);

        // Затем запросим историю у сервера и сравним историю менеджера и полученный ответ
        url = URI.create("http://localhost:8080/history");

        HttpRequest request0 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response = client.send(request0, HttpResponse.BodyHandlers.ofString());
        String historyFromResponse = response.body();
        String historyFromManager = gson.toJson(manager.getHistory());
        assertEquals(historyFromManager, historyFromResponse);
        assertEquals(200, response.statusCode());
    }

    private Task getTaskByIdWithHttpRequest(int id) throws IOException, InterruptedException {
        url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), Task.class);
    }

    private SubTask getSubTaskByIdWithHttpRequest(int id) throws IOException, InterruptedException {
        url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), SubTask.class);
    }

    private Epic getEpicByIdWithHttpRequest(int id) throws IOException, InterruptedException {
        url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), Epic.class);
    }

}
