package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.*;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_LIMIT = 10;
    //Спасибо за инфу про LinkedList и про особенности использования LinkedList и ArrayList, полезно!
    private final List<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() == HISTORY_LIMIT) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    //Получение истории просмотра задач
    @Override
    public List<Task> getHistory() {
       return new LinkedList<>(historyList);
    }

}
