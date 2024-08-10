package ru.practicum.taskTracker.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerInputException;
import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerOutputException;
import ru.practicum.taskTracker.exceptions.TaskManagerOverlappingException;
import ru.practicum.taskTracker.exceptions.TaskNotFoundException;
import ru.practicum.taskTracker.model.SubTask;
import ru.practicum.taskTracker.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubTaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();
            String[] pathParts = exchange.getRequestURI().getPath().split("/");

            if (pathParts.length == 2 && requestMethod.equals("GET")) {
                handleGetAllSubTasks(exchange);
            } else if (pathParts.length == 2 && requestMethod.equals("POST")) {
                handleAddOrUpdateSubTask(exchange);
            } else if (pathParts.length == 2 && requestMethod.equals("DELETE")) {
                handleRemoveAllSubTasks(exchange);
            } else if (pathParts.length == 3 && requestMethod.equals("GET")) {
                handleGetOneSubTask(exchange);
            } else if (pathParts.length == 3 && requestMethod.equals("DELETE")) {
                handleRemoveOneSubTask(exchange);
            }
        } catch (FileBackedTaskManagerInputException | FileBackedTaskManagerOutputException e) {
            sendInternalServerError(exchange);
        }
    }

    private void handleGetOneSubTask(HttpExchange exchange) {
        Integer id = getIdFromRequest(exchange);
        SubTask subTask;
        if (id == null) {
            sendNotFound(exchange, "Invalid subtask ID format");
            return;
        }
        try {
            subTask = manager.getSubTaskById(id);
            String response = getGson().toJson(subTask);
            sendText(exchange, response, 200);
        } catch (TaskNotFoundException e) {
            sendNotFound(exchange, "Subtask Not Found");
        }
    }

    private void handleAddOrUpdateSubTask(HttpExchange exchange) {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String jsonRequest = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            if (jsonRequest.isEmpty()) {
                sendText(exchange, "Bad Request", 400);
                return;
            }
            SubTask subTask = getGson().fromJson(jsonRequest, SubTask.class);
            if (subTask.getId() != 0 && subTask.getEpicId() != 0) {
                manager.updateSubTask(subTask);
                sendText(exchange, "Done", 201);
            } else if (subTask.getId() == 0 && subTask.getEpicId() != 0) {
                manager.addSubTask(subTask);
                sendText(exchange, "Done", 201);
            } else {
                sendNotFound(exchange, "Epic ID not identified");
            }
        } catch (TaskManagerOverlappingException e1) {
            sendHasInteractions(exchange);
        } catch (IOException e2) {
            sendInternalServerError(exchange);
        }
    }

    private void handleGetAllSubTasks(HttpExchange exchange) {
        String response = getGson().toJson(manager.getAllSubTasks());
        sendText(exchange, response, 200);
    }

    private void handleRemoveOneSubTask(HttpExchange exchange) {
        Integer id = getIdFromRequest(exchange);
        if (id != null) {
            try {
                manager.removeOneSubTaskById(id);
                sendText(exchange, "Done", 200);
            } catch (TaskNotFoundException e) {
                sendNotFound(exchange, "Subtask Not Found");
            }
        } else {
            sendNotFound(exchange, "Invalid subtask ID format");
        }
    }

    private void handleRemoveAllSubTasks(HttpExchange exchange) {
        manager.removeSubTasks();
        sendText(exchange, "Done", 200);
    }

}
