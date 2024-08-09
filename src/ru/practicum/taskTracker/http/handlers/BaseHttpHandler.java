package ru.practicum.taskTracker.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.taskTracker.http.adapters.DurationAdapter;
import ru.practicum.taskTracker.http.adapters.EpicDeserializer;
import ru.practicum.taskTracker.http.adapters.LocalDateTimeAdapter;
import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    TaskManager manager;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }

    protected void sendText(HttpExchange exchange, String text, int responseCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        sendText(exchange, text, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendText(exchange, "Tasks Are overlapping", 406);
    }

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        sendText(exchange, "Internal Server Error", 500);
    }

    protected Integer getIdFromRequest(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Integer.parseInt(pathParts[2]);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    protected Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(Epic.class, new EpicDeserializer());
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }

}