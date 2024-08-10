package ru.practicum.taskTracker.http;

import com.sun.net.httpserver.HttpServer;
import ru.practicum.taskTracker.exceptions.HttpException;
import ru.practicum.taskTracker.http.handlers.*;
import ru.practicum.taskTracker.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private TaskManager manager;
    private HttpServer httpServer;

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.createContext("/tasks", new TaskHandler(manager));
            httpServer.createContext("/epics", new EpicHandler(manager));
            httpServer.createContext("/subtasks", new SubTaskHandler(manager));
            httpServer.createContext("/history", new HistoryHandler(manager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
            httpServer.start();
        } catch (IOException e) {
            stop();
            throw new HttpException("Internal Server Error");
        }
    }

    public void stop() {
        httpServer.stop(0);
    }

}
