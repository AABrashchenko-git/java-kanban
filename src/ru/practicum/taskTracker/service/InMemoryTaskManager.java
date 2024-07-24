package ru.practicum.taskTracker.service;

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
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // d. Создание. Сам объект должен передаваться в качестве параметра.
    // 1) Задачи
    @Override
    public Task addTask(Task task) {
        task.setId(getNextId());
        if (task.getStartTime() != null)
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
        //TODO проверить последовательность
        if (subTask.getStartTime() != null)
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
        /*for (Integer subTaskEpicId : currentEpic.getSubTasksIdList()) {
            epic.addSubTaskIdToList(subTaskEpicId);
        }*/
        //TODO перенести в новый эпик временные метки со старого -> проверить
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
        /*Optional<Task> optionalTask = Optional.ofNullable(allTasks.get(taskId));
        optionalTask.ifPresent(task -> addToHistory(task));*/
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
/*        for (Task task : allTasks.values()) {
            historyManager.remove(task.getId());
        }*/
        allTasks.values().forEach(task -> historyManager.remove(task.getId()));
        allTasks.clear();
    }

    // 2) Эпики
    @Override
    public void removeEpics() {
/*        for (Epic epic : allEpics.values()) {
            historyManager.remove(epic.getId());
        }
        for (SubTask subTask : allSubTasks.values()) {
            historyManager.remove(subTask.getId());
        }*/
        allEpics.values().forEach(epic -> historyManager.remove(epic.getId()));
        allSubTasks.values().forEach(subTask -> historyManager.remove(subTask.getId()));

        allEpics.clear();
        allSubTasks.clear();
    }

    // 3) Подзадачи
    @Override
    public void removeSubTasks() {
/*        for (SubTask subTask : allSubTasks.values()) {
            historyManager.remove(subTask.getId());
        }*/
        allSubTasks.values().forEach(subTask -> historyManager.remove(subTask.getId()));
        allSubTasks.clear();
/*        for (Epic epic : allEpics.values()) {
            epic.getSubTasksIdList().clear();
            epic.setStatus(getUpdatedEpicStatus(epic));
            updateEpicTime(epic);
        }*/
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
        allTasks.remove(taskId);
    }

    // 2) Эпики
    @Override
    public void removeOneEpicById(int epicId) {
        if (!allEpics.containsKey(epicId)) {
            return;
        }
        Epic epic = allEpics.get(epicId);
/*        for (Integer subTaskId : epic.getSubTasksIdList()) {
            allSubTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        }*/
        epic.getSubTasksIdList().forEach(subTaskId -> {
            allSubTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        });

        epic.getSubTasksIdList().clear();
        historyManager.remove(epic.getId());
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
        epic.getSubTasksIdList().remove((Object) subTask.getId());
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
/*        for (SubTask subTask : allSubTasks.values()) {
            if (subTask.getEpicId() == epic.getId()) {
                subTasksOfEpicList.add(subTask);
            }
        }*/
        allSubTasks.values().stream()
                .filter(subTask -> (subTask.getEpicId() == epic.getId()))
                .forEach(subTasksOfEpicList::add);
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
        List<SubTask> subTasksSortedByEndTime = getAllSubTaskOfEpic(epic.getId()).stream()
                .filter(subTask -> (subTask.getStartTime() != null && subTask.getEndTime() != null))
                .sorted(Comparator.comparing(SubTask::getEndTime)).toList();
        List<SubTask> subTasksSortedByStartTime = getAllSubTaskOfEpic(epic.getId()).stream()
                .filter(subTask -> (subTask.getStartTime() != null && subTask.getEndTime() != null))
                .sorted(Comparator.comparing(SubTask::getStartTime)).toList();

        if (!subTasksSortedByStartTime.isEmpty() && !subTasksSortedByEndTime.isEmpty()) {
            LocalDateTime epicStartTime = subTasksSortedByStartTime.getFirst().getStartTime();
            LocalDateTime epicEndTime = subTasksSortedByEndTime.getLast().getEndTime();
            Duration epicDuration = Duration.between(epicStartTime, epicEndTime);
            epic.setStartTime(epicStartTime);
            epic.setEndTime(epicEndTime);
            epic.setDuration(epicDuration);
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void addPrioritizedTask(Task taskToAdd) {
        boolean isValid = getPrioritizedTasks()
                .stream().filter(task -> isOverlapping(taskToAdd, task)).toList().isEmpty();
        if (isValid) {
            prioritizedTasks.add(taskToAdd);
        } else {
            // Как вариант, если мы все-таки хотим добавит задачу, то устанавливаем ей время начала,
            // соответствующее времени окончания последнего элемента в prioritizedTasks
            taskToAdd.setStartTime(prioritizedTasks.stream().toList().getLast().getEndTime());
            prioritizedTasks.add(taskToAdd);
            //TODO проверить на null
        }
    }

    public boolean isOverlapping(Task task1, Task task2) {
        // task1 начинается раньше task2, но task2 кончается после task1
        boolean isOverlappingByEnd = task1.getStartTime().isBefore(task2.getStartTime())
                && task1.getEndTime().isAfter(task2.getStartTime()) && task1.getEndTime().isBefore(task2.getEndTime());
        // task1 начинается позже task2, но task2 кончается раньше task1
        boolean isOverlappingByStart = task1.getStartTime().isAfter(task2.getStartTime())
                && task1.getEndTime().isAfter(task2.getEndTime()) && task1.getStartTime().isBefore(task2.getEndTime());
        // task1 и task2 находятся одна внутри другой
        boolean areTasksWithin = (task1.getStartTime().isAfter(task2.getStartTime()) && task1.getEndTime().isBefore(task2.getEndTime()))
                || (task1.getStartTime().isBefore(task2.getStartTime()) && task1.getEndTime().isAfter(task2.getEndTime()));
        // временные интервалы у task1 и task2 совпадают
        boolean areTasksTimesEqual = task1.getStartTime().equals(task2.getStartTime()) && task1.getEndTime().equals(task2.getEndTime());

        return isOverlappingByEnd || isOverlappingByStart || areTasksWithin || areTasksTimesEqual;
    }
}
