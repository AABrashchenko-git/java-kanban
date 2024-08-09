package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.taskTracker.http.HttpTaskServer;
import ru.practicum.taskTracker.http.adapters.*;
import ru.practicum.taskTracker.model.Epic;
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

public class HttpEpicHandlerTest {
    private TaskManager manager;
    private HttpTaskServer server;
    HttpClient client;
    URI url;
    Gson gson;
    Epic epic1;
    Epic epic2;

    @BeforeEach
    void beforeEach() throws IOException {
        manager = Managers.getFileBackedManager();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/epics");
        epic1 = new Epic("Test Name", "Test Description");
        epic2 = new Epic("Test Name2", "Test Description2");
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
    void testHandleGetOneEpic() throws IOException, InterruptedException {
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        Epic epicFromResponse1 = gson.fromJson(response1.body(), Epic.class);
        assertEquals(manager.getEpicById(1), epicFromResponse1);
        assertEquals(200, response1.statusCode());

        url = URI.create("http://localhost:8080/epics/2");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Epic epicFromResponse2 = gson.fromJson(response2.body(), Epic.class);
        assertEquals(manager.getEpicById(2), epicFromResponse2);
        assertEquals(200, response2.statusCode());
        // Получим несуществующую задачу
        url = URI.create("http://localhost:8080/epics/999");
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response3.statusCode());
        assertThrows(JsonSyntaxException.class, () ->
                gson.fromJson(response3.body(), Epic.class));
    }

    @Test
    void testHandleAddOrUpdateEpic() throws InterruptedException, IOException {
        // Тестируем добавление задач
        String epicJson1 = gson.toJson(epic1);
        String epicJson2 = gson.toJson(epic2);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson1)).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response1.statusCode());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson2)).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response2.statusCode());

        List<Epic> epicsFromManager = manager.getAllEpics();
        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(2, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Test Name", epicsFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Test Name2", epicsFromManager.get(1).getName(), "Некорректное имя задачи");

        // Тестируем обновление задач
        String epicJsonUpd1 = gson.toJson(new Epic(1, "New Test Name 1", "New Test Description1"));
        String epicJsonUpd2 = gson.toJson(new Epic(2, "New Test Name 2", "New Test Description1"));
        HttpRequest requestUpd1 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJsonUpd1)).build();
        HttpResponse<String> responseUpd1 = client.send(requestUpd1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpd1.statusCode());

        HttpRequest requestUpd2 = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJsonUpd2)).build();
        HttpResponse<String> responseUpd2 = client.send(requestUpd2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseUpd2.statusCode());

        assertNotNull(manager.getAllEpics(), "Задачи не возвращаются");
        assertEquals(2, manager.getAllEpics().size(), "Некорректное количество задач");
        assertEquals("New Test Name 1", manager.getAllEpics().get(0).getName(), "Некорректное имя задачи");
        assertEquals("New Test Name 2", manager.getAllEpics().get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    void testHandleGetAllEpics() throws IOException, InterruptedException {
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        List<Epic> epicsFromResponse = gson.fromJson(response1.body(), new TypeToken<List<Epic>>() {
        }.getType());
        assertEquals(manager.getAllEpics(), epicsFromResponse);
        assertEquals(200, response1.statusCode());
    }

    @Test
    void testHandleRemoveOneEpic() throws IOException, InterruptedException {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        assertEquals(2, manager.getAllEpics().size());

        url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());
        assertEquals(1, manager.getAllEpics().size());
        // Попробуем удалить еще раз ту же задачу
        url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response2.statusCode());
    }

    @Test
    void testHandleRemoveAllEpics() throws IOException, InterruptedException {
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).DELETE().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(manager.getAllEpics(), Collections.emptyList());
        assertEquals(200, response1.statusCode());
    }

    @Test
    void handleGetEpicSubtasks() throws IOException, InterruptedException {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubTask(new SubTask(epic1.getId(), "имяПодзадачи2Эпика1",
                "описаниеПодзадачи2Эпика1", LocalDateTime.now().plusMinutes(61), Duration.ofMinutes(9)));
        manager.addSubTask(new SubTask(epic1.getId(), "имяПодзадачи3Эпика1",
                "описаниеПодзадачи1Эпика2", LocalDateTime.now().plusMinutes(71), Duration.ofMinutes(59)));
        manager.addSubTask(new SubTask(epic1.getId(), "имяПодзадачи1Эпика2",
                "описаниеПодзадачи1Эпика2", LocalDateTime.now().plusMinutes(240), Duration.ofMinutes(60)));

        url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTasksFromResponse = gson.fromJson(response1.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(manager.getAllSubTaskOfEpic(1), subTasksFromResponse);
        assertEquals(200, response1.statusCode());

        url = URI.create("http://localhost:8080/epics/2/subtasks");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTasksFromResponse2 = gson.fromJson(response2.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(manager.getAllSubTaskOfEpic(2), subTasksFromResponse2);
        assertEquals(200, response2.statusCode());
    }

}
