package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpPrioritizedHandlerTest {
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
        url = URI.create("http://localhost:8080/prioritized");
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
        manager.addTask(subTask1);
        manager.addTask(subTask2);
        manager.addTask(subTask3);
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
    void testHandlePrioritized() throws IOException, InterruptedException {
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        Set<Task> tasksFromResponse = gson.fromJson(response1.body(), new TypeToken<Set<Task>>() {
        }.getType());
        assertEquals(manager.getPrioritizedTasks(), tasksFromResponse);
        assertEquals(200, response1.statusCode());
    }
}
