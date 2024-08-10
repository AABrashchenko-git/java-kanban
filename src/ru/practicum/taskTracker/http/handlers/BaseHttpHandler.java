package ru.practicum.taskTracker.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.taskTracker.http.adapters.DurationAdapter;
import ru.practicum.taskTracker.http.adapters.EpicDeserializer;
import ru.practicum.taskTracker.http.adapters.LocalDateTimeAdapter;
import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.service.TaskManager;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

class BaseHttpHandler {
    TaskManager manager;

    BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
    }

    protected void sendText(HttpExchange exchange, String text, int responseCode) {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, resp.length);
            exchange.getResponseBody().write(resp);
            exchange.close();
        } catch (IOException e) {
            sendInternalServerError(exchange);
        }
    }

    protected void sendNotFound(HttpExchange exchange, String text) {
        sendText(exchange, text, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) {
        sendText(exchange, "Tasks Are overlapping", 406);
    }

    protected void sendInternalServerError(HttpExchange exchange) {
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

    // По твоей рекомендации попробовал сделать метод для десериализации эпика с помощью JsonReader
    // Применил в handleAddOrUpdateEpic() класса EpicHandler, вроде работает)) Спасибо за инфу!
    public Epic readEpic(HttpExchange exchange, String json) {
        JsonReader reader = new JsonReader(new StringReader(json));
        int id = 0;
        String name = null;
        String description = null;
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String fieldName = reader.nextName();
                if (fieldName.equals("id")) {
                    id = reader.nextInt();
                } else if (fieldName.equals("name")) {
                    name = reader.nextString();
                } else if (fieldName.equals("description")) {
                    description = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            sendInternalServerError(exchange);
        }
        return new Epic(id, name, description);
    }

}