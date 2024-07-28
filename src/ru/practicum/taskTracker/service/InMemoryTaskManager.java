package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.exceptions.TaskManagerOverlappingException;
import ru.practicum.taskTracker.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int taskCounter;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> allTasks = new HashMap<>();
    protected final Map<Integer, Epic> allEpics = new HashMap<>();
    protected final Map<Integer, SubTask> allSubTasks = new HashMap<>();
    private final Comparator<Task> taskComparator = Comparator
            .comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Task::getId);

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // d. Создание. Сам объект должен передаваться в качестве параметра.
    // 1) Задачи
    @Override
    public Task addTask(Task task) {
        task.setId(getNextId());
        addPrioritizedTask(task);
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
        addPrioritizedTask(subTask);
        epic.addSubTaskIdToList(subTask.getId());
        epic.setStatus(getUpdatedEpicStatus(epic));
        updateEpicTime(epic);
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
        addPrioritizedTask(task);
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
        currentEpic.getSubTasksIdList().forEach(epic::addSubTaskIdToList);
        epic.setStatus(getUpdatedEpicStatus(epic));
        updateEpicTime(epic);
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
        addPrioritizedTask(subTask);
        epicOfSubTask.setStatus(getUpdatedEpicStatus(epicOfSubTask));
        updateEpicTime(epicOfSubTask);
        return subTask;
    }

    // c. Получение по идентификатору.
    // 1) Задачи
    @Override
    public Task getTaskById(int taskId) {
        Task task = allTasks.get(taskId);
        // Добавляем задачу в историю
        if (task != null) {
            addToHistory(task);
        }
        return task;
    }

    // 2) Эпики
    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = allEpics.get(epicId);
        // Добавляем эпик в историю
        if (epic != null) {
            addToHistory(epic);
        }
        return epic;
    }

    // 3) Подзадачи
    @Override
    public SubTask getSubTaskById(int subTaskId) {
        SubTask subTask = allSubTasks.get(subTaskId);
        if (subTask != null) {
            addToHistory(subTask);
        }
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
        allTasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        allTasks.clear();
    }

    // 2) Эпики
    @Override
    public void removeEpics() {
        allEpics.values().forEach(epic -> historyManager.remove(epic.getId()));
        allSubTasks.values().forEach(subTask -> {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
        allEpics.clear();
        allSubTasks.clear();
    }

    // 3) Подзадачи
    @Override
    public void removeSubTasks() {
        allSubTasks.values().forEach(subTask -> {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        });
        allSubTasks.clear();
        allEpics.values().forEach(epic -> {
            epic.getSubTasksIdList().clear();
            epic.setStatus(getUpdatedEpicStatus(epic));
            updateEpicTime(epic);
        });
    }

    // f. Удаление по идентификатору.
    // 1) Задачи
    @Override
    public void removeOneTaskById(int taskId) {
        if (!allTasks.containsKey(taskId)) {
            return;
        }
        historyManager.remove(taskId);
        prioritizedTasks.remove(allTasks.get(taskId));
        allTasks.remove(taskId);
    }

    // 2) Эпики
    @Override
    public void removeOneEpicById(int epicId) {
        if (!allEpics.containsKey(epicId)) {
            return;
        }
        Epic epic = allEpics.remove(epicId);
        epic.getSubTasksIdList().forEach(subTaskId -> {
            prioritizedTasks.remove(allSubTasks.get(subTaskId));
            allSubTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        });

        epic.getSubTasksIdList().clear();
        historyManager.remove(epic.getId());
    }

    // 3) Подзадачи
    @Override
    public void removeOneSubTaskById(int subTaskId) {
        SubTask subTask = allSubTasks.get(subTaskId);
        if (subTask == null) {
            return;
        }
        Epic epic = allEpics.get(subTask.getEpicId());
        epic.getSubTasksIdList().remove((Object) subTask.getId());
        prioritizedTasks.remove(allSubTasks.get(subTaskId));
        allSubTasks.remove(subTaskId);
        historyManager.remove(subTask.getId());
        epic.setStatus(getUpdatedEpicStatus(epic));
        updateEpicTime(epic);
    }

    // Получение всех подзадач эпика
    @Override
    public ArrayList<SubTask> getAllSubTaskOfEpic(int epicId) {
        Epic epic = allEpics.get(epicId);
        if (epic == null) {
            return null;
        }
        ArrayList<SubTask> subTasksOfEpicList = new ArrayList<>();
        epic.getSubTasksIdList().forEach(subTaskId -> {
            subTasksOfEpicList.add(allSubTasks.get(subTaskId));
        });
        return subTasksOfEpicList;
    }

    @Override
    public int getNextId() {
        return ++taskCounter;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void addToHistory(Task task) {
        historyManager.add(task);
    }

    public void setTaskCounter(int taskCounter) {
        this.taskCounter = taskCounter;
    }

    public Status getUpdatedEpicStatus(Epic epic) {
        int newCount = 0;
        int doneCount = 0;
        int inProgressCount = 0;

        if (epic.getSubTasksIdList().isEmpty()) {
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

    public void updateEpicTime(Epic epic) {
        List<SubTask> subTasks = getAllSubTaskOfEpic(epic.getId());
        LocalDateTime epicStartTime = null;
        LocalDateTime epicEndTime = null;

        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime() != null
                    && (epicStartTime == null || subTask.getStartTime().isBefore(epicStartTime))) {
                epicStartTime = subTask.getStartTime();
            }
            if (subTask.getEndTime() != null
                    && (epicEndTime == null || subTask.getEndTime().isAfter(epicEndTime))) {
                epicEndTime = subTask.getEndTime();
            }
        }
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        if (epicStartTime != null && epicEndTime != null)
            epic.setDuration(Duration.between(epicStartTime, epicEndTime));
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return new TreeSet<>(prioritizedTasks);
    }

    public void addPrioritizedTask(Task taskToAdd) {
        if (!isOverlapping(taskToAdd)) { //если не пересекатся по времени
            // удалить старую, если существует в списке, иначе set оставит старую, новая не добавится
            prioritizedTasks.removeIf(task -> task.getId() == taskToAdd.getId());
            prioritizedTasks.add(taskToAdd);
        } else {
            throw new TaskManagerOverlappingException("Task overlaps with an existing task.");
        }
    }

    private boolean isOverlapping(Task taskToCheck) {
        for (Task task : getPrioritizedTasks()) {
            if (taskToCheck.getStartTime() == null || task.getStartTime() == null)
                return false;
            if (taskToCheck.getId() == task.getId())
                return false;
            if (!taskToCheck.getEndTime().isAfter(task.getStartTime()))
                continue;
            if (!taskToCheck.getStartTime().isBefore(task.getEndTime()))
                continue;
            if (taskToCheck.getEndTime().isAfter(task.getStartTime())
                    && taskToCheck.getStartTime().isBefore(task.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    public Optional<Task> getTaskByIdOptional(int taskId) {
        Optional<Task> optionalTask = Optional.ofNullable(allTasks.get(taskId));
        optionalTask.ifPresent(this::addToHistory);
        return optionalTask;
    }

    public Optional<Epic> getEpicByIdOptional(int epicId) {
        Optional<Epic> optionalEpic = Optional.ofNullable(allEpics.get(epicId));
        optionalEpic.ifPresent(this::addToHistory);
        return optionalEpic;
    }

    public Optional<SubTask> getSubTaskByIdOptional(int subTaskId) {
        Optional<SubTask> optionalSubTask = Optional.ofNullable(allSubTasks.get(subTaskId));
        optionalSubTask.ifPresent(this::addToHistory);
        return optionalSubTask;
    }
}
