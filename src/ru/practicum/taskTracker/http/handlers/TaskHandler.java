package ru.practicum.taskTracker.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerInputException;
import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerOutputException;
import ru.practicum.taskTracker.exceptions.TaskManagerOverlappingException;
import ru.practicum.taskTracker.exceptions.TaskNotFoundException;
import ru.practicum.taskTracker.model.Task;
import ru.practicum.taskTracker.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();
            String[] pathParts = exchange.getRequestURI().getPath().split("/");

            if (pathParts.length == 2 && requestMethod.equals("GET")) {
                handleGetAllTasks(exchange);
            } else if (pathParts.length == 2 && requestMethod.equals("POST")) {
                handleAddOrUpdateTask(exchange);
            } else if (pathParts.length == 2 && requestMethod.equals("DELETE")) {
                handleRemoveAllTasks(exchange);
            } else if (pathParts.length == 3 && requestMethod.equals("GET")) {
                handleGetOneTask(exchange);
            } else if (pathParts.length == 3 && requestMethod.equals("DELETE")) {
                handleRemoveOneTask(exchange);
            }
        } catch (FileBackedTaskManagerInputException | FileBackedTaskManagerOutputException e) {
            sendInternalServerError(exchange);
        }
    }

    private void handleGetOneTask(HttpExchange exchange) {
        Integer id = getIdFromRequest(exchange);
        Task task;
        if (id == null) {
            sendNotFound(exchange, "Invalid task ID format");
            return;
        }
        try {
            task = manager.getTaskById(id);
            String response = getGson().toJson(task);
            sendText(exchange, response, 200);
        } catch (TaskNotFoundException e) {
            sendNotFound(exchange, "Task Not Found");
        }
    }

    private void handleAddOrUpdateTask(HttpExchange exchange) {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String jsonRequest = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            if (jsonRequest.isEmpty()) {
                sendText(exchange, "Bad Request", 400);
                return;
            }
            Task task = getGson().fromJson(jsonRequest, Task.class);
            if (task.getId() != 0) {
                manager.updateTask(task);
                sendText(exchange, "Done", 201);

            } else {
                manager.addTask(task);
                sendText(exchange, "Done", 201);
            }
        } catch (TaskManagerOverlappingException e) {
            sendHasInteractions(exchange);
        } catch (IOException ioe) {
            sendInternalServerError(exchange);
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) {
        String response = getGson().toJson(manager.getAllTasks());
        sendText(exchange, response, 200);
    }

    private void handleRemoveOneTask(HttpExchange exchange) {
        Integer id = getIdFromRequest(exchange);
        if (id != null) {
            try {
                manager.removeOneTaskById(id);
                sendText(exchange, "Done", 200);
            } catch (TaskNotFoundException e) {
                sendNotFound(exchange, "Task Not Found");
            }
        } else {
            sendNotFound(exchange, "Invalid task ID format");
        }
    }

    private void handleRemoveAllTasks(HttpExchange exchange) {
        manager.removeTasks();
        sendText(exchange, "Done", 200);
    }

}
