package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.model.Status;
import ru.practicum.taskTracker.model.SubTask;
import ru.practicum.taskTracker.model.Task;

import java.util.*;

public class TaskManager {
    private static int taskCounter;
    private static final HashMap<Integer, Task> allTasks = new HashMap<>();
    private static final HashMap<Integer, Epic> allEpics = new HashMap<>();
    private static final HashMap<Integer, SubTask> allSubTasks = new HashMap<>();

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // d. Создание. Сам объект должен передаваться в качестве параметра.
    // 1) Задачи
    public static Task addTask(Task task) {
        task.setId(getNextId());
        return allTasks.put(task.getId(), task);
    }

    // 2) Эпики
    public static Epic addEpic(Epic epic) {
        epic.setId(getNextId());
        return allEpics.put(epic.getId(), epic);
    }

    // 3) Подзадачи
    public static SubTask addSubTask(SubTask subTask) {
        subTask.setId(getNextId());
        int epicId = subTask.getEpicId();

        Epic epic = allEpics.get(epicId);
        epic.addSubTaskToList(subTask);
        allSubTasks.put(subTask.getId(), subTask);
        epic.setStatus(getUpdatedEpicStatus(epic));
        return subTask;
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    // 1) Задачи
    public static Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !allTasks.containsKey(taskId)) {
            return null;
        }
        return allTasks.replace(task.getId(), task);
    }

    // 2) Эпики
    public static Epic updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epicId == null || !allEpics.containsKey(epicId)) {
            return null;
        }
        Epic epicToUpdate = allEpics.get(epic.getId());
        epicToUpdate.setName(epic.getName());
        epicToUpdate.setDescription(epic.getDescription());
        return epicToUpdate;
    }

    // 3) Подзадачи
    public static SubTask updateSubTask(SubTask subTask) {
        Epic epicOfSubTask = allEpics.get(subTask.getEpicId());
        Integer subTaskId = subTask.getId();
        if (subTaskId == null || epicOfSubTask == null) {
            return null;
        }
        allSubTasks.replace(subTask.getId(), subTask);
        epicOfSubTask.replaceElementInSubTaskList(subTask);
        epicOfSubTask.setStatus(getUpdatedEpicStatus(epicOfSubTask));
        return subTask;
    }

    // c. Получение по идентификатору.
    // 1) Задачи
    public static Task getTaskById(int taskId) {
        return allTasks.get(taskId);
    }

    // 2) Эпики
    public static Epic getEpicById(int epicId) {
        return allEpics.get(epicId);
    }

    // 3) Подзадачи
    public static SubTask getSubTaskById(int subTaskId) {
        return allSubTasks.get(subTaskId);
    }

    // a. Получение списка всех задач.
    // 1) Задачи
    public static ArrayList<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    // 2) Эпики
    public static ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());
    }

    // 3) Подзадачи
    public static ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(allSubTasks.values());
    }

    // b. Удаление всех задач.
    // 1) Задачи
    public static void removeTasks() {
        allTasks.clear();
    }

    // 2) Эпики
    public static void removeEpics() {
        allEpics.clear();
        allSubTasks.clear();
    }

    // 3) Подзадачи
    public static void removeSubTasks() {
        allSubTasks.clear();
        for (Epic epic : allEpics.values()) {
            epic.getSubTasksList().clear();
            epic.setStatus(getUpdatedEpicStatus(epic));
        }
    }

    // f. Удаление по идентификатору.
    // 1) Задачи
    public static void removeOneTaskById(int taskId) {
        allTasks.remove(taskId);
    }

    // 2) Эпики
    public static void removeOneEpicById(int epicId) {
        Epic epic = getEpicById(epicId);
        for (SubTask subTask : epic.getSubTasksList()) {
            allSubTasks.remove(subTask);
        }
        epic.getSubTasksList().clear();
        allEpics.remove(epicId);
    }

    // 3) Подзадачи
    public static void removeOneSubTaskById(int subTaskId) {
        SubTask subTask = getSubTaskById(subTaskId);
        if (subTask == null) {
            return;
        }
        Epic epic = getEpicById(subTask.getEpicId());
        epic.getSubTasksList().remove(subTask);
        allSubTasks.remove(subTaskId);
        epic.setStatus(getUpdatedEpicStatus(epic));
    }

    // Получение всех подзадач эпика
    public static ArrayList<SubTask> getAllSubTaskOfEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        if (epic == null) {
            return null;
        }
        ArrayList<SubTask> allEpicSubTasks = new ArrayList<>();
        for (SubTask subTask : allSubTasks.values()) {
            if (subTask.getEpicId() == epicId) {
                allEpicSubTasks.add(subTask);
            }
        }
        return allEpicSubTasks;
    }

    public static int getNextId() {
        return ++taskCounter;
    }

    public static Status getUpdatedEpicStatus(Epic epic) {
        int newCount = 0;
        int doneCount = 0;
        int inProgressCount = 0;

        if (epic.getSubTasksList().isEmpty()) {
            return Status.NEW;
        }
        ArrayList<SubTask> subTasksOfEpic = getAllSubTaskOfEpic(epic.getId());

        for (SubTask subTask : subTasksOfEpic) {
            if (subTask.getStatus() == Status.NEW) {
                newCount++;
            } else if (subTask.getStatus() == Status.DONE) {
                doneCount++;
            } else {
                inProgressCount++;
            }
        }
        if (newCount > 0 && doneCount == 0 && inProgressCount == 0) {
            return Status.NEW;
        } else if (inProgressCount == 0 && newCount == 0 && doneCount > 0) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }
}
