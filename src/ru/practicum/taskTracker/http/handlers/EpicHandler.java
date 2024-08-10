package ru.practicum.taskTracker.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerInputException;
import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerOutputException;
import ru.practicum.taskTracker.exceptions.TaskNotFoundException;
import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                handleGetEpicSubtasks(exchange);
            } else if (pathParts.length == 2 && requestMethod.equals("GET")) {
                handleGetAllEpics(exchange);
            } else if (pathParts.length == 2 && requestMethod.equals("POST")) {
                handleAddOrUpdateEpic(exchange);
            } else if (pathParts.length == 2 && requestMethod.equals("DELETE")) {
                handleRemoveAllEpics(exchange);
            } else if (pathParts.length == 3 && requestMethod.equals("GET")) {
                handleGetOneEpic(exchange);
            } else if (pathParts.length == 3 && requestMethod.equals("DELETE")) {
                handleRemoveOneEpic(exchange);
            }
        } catch (FileBackedTaskManagerInputException | FileBackedTaskManagerOutputException e) {
            sendInternalServerError(exchange);
        }
    }

    private void handleGetOneEpic(HttpExchange exchange) {
        Integer id = getIdFromRequest(exchange);
        Epic epic;
        if (id == null) {
            sendNotFound(exchange, "Invalid epic ID format");
            return;
        }
        try {
            epic = manager.getEpicById(id);
            String response = getGson().toJson(epic);
            sendText(exchange, response, 200);
        } catch (TaskNotFoundException e) {
            sendNotFound(exchange, "Epic Not Found");
        }
    }

    private void handleAddOrUpdateEpic(HttpExchange exchange) {
        try (InputStream inputStream = exchange.getRequestBody()) {
            String jsonRequest = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            if (jsonRequest.isEmpty()) {
                sendText(exchange, "Bad Request", 400);
                return;
            }
            Epic epic = readEpic(exchange, jsonRequest);
            //Epic epic = getGson().fromJson(jsonRequest, Epic.class);
            if (epic.getId() != 0) {
                manager.updateEpic(epic);
                sendText(exchange, "Done", 201);
            } else {
                manager.addEpic(epic);
                sendText(exchange, "Done", 201);
            }
        } catch (IOException e) {
            sendInternalServerError(exchange);
        }
    }

    private void handleGetAllEpics(HttpExchange exchange) {
        String response = getGson().toJson(manager.getAllEpics());
        sendText(exchange, response, 200);
    }

    private void handleRemoveOneEpic(HttpExchange exchange) {
        Integer id = getIdFromRequest(exchange);
        if (id != null) {
            try {
                manager.removeOneEpicById(id);
                sendText(exchange, "Done", 200);
            } catch (TaskNotFoundException e) {
                sendNotFound(exchange, "Epic Not Found");
            }
        } else {
            sendNotFound(exchange, "Invalid Epic ID format");
        }
    }

    private void handleRemoveAllEpics(HttpExchange exchange) {
        manager.removeEpics();
        sendText(exchange, "Done", 200);
    }

    public void handleGetEpicSubtasks(HttpExchange exchange) {
        Integer epicId = getIdFromRequest(exchange);
        if (epicId != null) {
            String response = getGson().toJson(manager.getAllSubTaskOfEpic(epicId));
            sendText(exchange, response, 200);
        } else {
            sendNotFound(exchange, "Invalid Epic ID format");
        }
    }

}
