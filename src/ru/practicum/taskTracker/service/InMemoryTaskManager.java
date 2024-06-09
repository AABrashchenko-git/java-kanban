package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int taskCounter;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Task> allTasks = new HashMap<>();
    private final HashMap<Integer, Epic> allEpics = new HashMap<>();
    private final HashMap<Integer, SubTask> allSubTasks = new HashMap<>();

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // d. Создание. Сам объект должен передаваться в качестве параметра.
    // 1) Задачи
    @Override
    public Task addTask(Task task) {
        task.setId(getNextId());
        return allTasks.put(task.getId(), task);
    }

    // 2) Эпики
    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(getNextId());
        return allEpics.put(epic.getId(), epic);
    }

    // 3) Подзадачи
    @Override
    public SubTask addSubTask(SubTask subTask) {
        subTask.setId(getNextId());
        allSubTasks.put(subTask.getId(), subTask);
        int epicId = subTask.getEpicId();
        Epic epic = allEpics.get(epicId);
        if (epic == null) {
            return null;
        }
        epic.addSubTaskToList(subTask);
        epic.setStatus(getUpdatedEpicStatus(epic));
        return subTask;
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    // 1) Задачи
    @Override
    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !allTasks.containsKey(taskId)) {
            return null;
        }
        allTasks.replace(task.getId(), task);
        return task;
    }

    // 2) Эпики
    @Override
    public Epic updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epicId == null || !allEpics.containsKey(epicId)) {
            return null;
        }
        Epic currentEpic = allEpics.get(epic.getId());
        allEpics.replace(epic.getId(), epic);
        for(SubTask subTask : currentEpic.getSubTasksList()) {
            epic.addSubTaskToList(subTask);
        }
        return epic;
    }

    // 3) Подзадачи
    @Override
    public SubTask updateSubTask(SubTask subTask) {
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
    @Override
    public Task getTaskById(int taskId) {
        Task task = allTasks.get(taskId);
        // Добавляем задачу в историю
        historyManager.add(task);
        return task;
    }

    // 2) Эпики
    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = allEpics.get(epicId);
        // Добавляем эпик в историю
        historyManager.add(epic);
        return epic;
    }

    // 3) Подзадачи
    @Override
    public SubTask getSubTaskById(int subTaskId) {
        SubTask subTask = allSubTasks.get(subTaskId);
        historyManager.add(subTask);
        return subTask;
    }

    // a. Получение списка всех задач.
    // 1) Задачи
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    // 2) Эпики
    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());
    }

    // 3) Подзадачи
    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(allSubTasks.values());
    }

    // b. Удаление всех задач.
    // 1) Задачи
    @Override
    public void removeTasks() {
        allTasks.clear();
    }

    // 2) Эпики
    @Override
    public void removeEpics() {
        allEpics.clear();
        allSubTasks.clear();
    }

    // 3) Подзадачи
    @Override
    public void removeSubTasks() {
        allSubTasks.clear();
        for (Epic epic : allEpics.values()) {
            epic.getSubTasksList().clear();
            epic.setStatus(getUpdatedEpicStatus(epic));
        }
    }

    // f. Удаление по идентификатору.
    // 1) Задачи
    @Override
    public void removeOneTaskById(int taskId) {
        if (!allTasks.containsKey(taskId)) {
            return;
        }
        allTasks.remove(taskId);
    }

    // 2) Эпики
    @Override
    public void removeOneEpicById(int epicId) {
        if (!allEpics.containsKey(epicId)) {
            return;
        }
        Epic epic = allEpics.get(epicId);
        for (SubTask subTask : epic.getSubTasksList()) {
            allSubTasks.remove(subTask);
        }
        epic.getSubTasksList().clear();
        allEpics.remove(epicId);

    }

    // 3) Подзадачи
    @Override
    public void removeOneSubTaskById(int subTaskId) {
        SubTask subTask = allSubTasks.get(subTaskId);
        if (subTask == null) {
            return;
        }
        Epic epic = allEpics.get(subTask.getEpicId());
        epic.getSubTasksList().remove(subTask);
        allSubTasks.remove(subTaskId);
        epic.setStatus(getUpdatedEpicStatus(epic));
    }

    // Получение всех подзадач эпика
    @Override
    public ArrayList<SubTask> getAllSubTaskOfEpic(int epicId) {
        Epic epic = allEpics.get(epicId);
        if (epic == null) {
            return null;
        }
        return epic.getSubTasksList();
    }

    @Override
    public int getNextId() {
        return ++taskCounter;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public Status getUpdatedEpicStatus(Epic epic) {
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
