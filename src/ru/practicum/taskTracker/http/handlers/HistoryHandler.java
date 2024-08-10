package ru.practicum.taskTracker.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.practicum.taskTracker.service.TaskManager;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String response = getGson().toJson(manager.getHistory());
        sendText(exchange, response, 200);
    }

}
