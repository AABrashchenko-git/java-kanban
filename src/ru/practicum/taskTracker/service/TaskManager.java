package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    SubTask addSubTask(SubTask subTask);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    SubTask updateSubTask(SubTask subTask);

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    SubTask getSubTaskById(int subTaskId);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

    void removeTasks();

    void removeEpics();

    void removeSubTasks();

    void removeOneTaskById(int taskId);

    void removeOneEpicById(int epicId);

    void removeOneSubTaskById(int subTaskId);

    ArrayList<SubTask> getAllSubTaskOfEpic(int epicId);

    int getNextId();

    List<Task> getHistory();
}
