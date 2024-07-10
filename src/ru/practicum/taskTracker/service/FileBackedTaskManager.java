package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerInputException;
import ru.practicum.taskTracker.exceptions.FileBackedTaskManagerOutputException;
import ru.practicum.taskTracker.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public Task addTask(Task task) {
        Task addedTask = super.addTask(task);
        save();
        return addedTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic addedEpic = super.addEpic(epic);
        save();
        return addedEpic;
    }

    @Override
    public SubTask addSubTask(SubTask subTask) {
        SubTask subTaskAdded = super.addSubTask(subTask);
        save();
        return subTaskAdded;
    }

    @Override
    public Task updateTask(Task task) {
        Task taskUpdated = super.updateTask(task);
        save();
        return taskUpdated;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic epicUpdated = super.updateEpic(epic);
        save();
        return epicUpdated;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {
        SubTask subTaskUpdated = super.updateSubTask(subTask);
        save();
        return subTaskUpdated;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task taskById = super.getTaskById(taskId);
        save();
        return taskById;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epicById = super.getEpicById(epicId);
        save();
        return epicById;
    }

    @Override
    public SubTask getSubTaskById(int subTaskId) {
        SubTask subTaskById = super.getSubTaskById(subTaskId);
        save();
        return subTaskById;
    }

    @Override
    public void removeOneTaskById(int taskId) {
        super.removeOneTaskById(taskId);
        save();
    }

    @Override
    public void removeOneEpicById(int epicId) {
        super.removeOneEpicById(epicId);
        save();
    }

    @Override
    public void removeOneSubTaskById(int subTaskId) {
        super.removeOneSubTaskById(subTaskId);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    // 2) Эпики
    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubTasks() {
        super.removeSubTasks();
        save();
    }

    @Override
    public void addToHistory(Task task) {
        super.addToHistory(task);
        save();
    }

    // СПРИНТ 7
    // Метод сохранения задачи в строку
    public String toString(Task task) {
        StringBuilder taskToString = new StringBuilder(String.format("%s,%s,%s,%s,%s", task.getId(), task.getType(),
                task.getStatus(), task.getName(), task.getDescription()));

        switch (task.getType()) {
            case SUBTASK:
                taskToString.append(",").append(((SubTask) task).getEpicId());
                break;
            case EPIC:
                taskToString.append(",");
                for (Integer subTaskId : ((Epic) task).getSubTasksIdList()) {
                    taskToString.append("st").append(subTaskId);
                }
                break;
        }
        return taskToString.append("\n").toString();
    }

    // Метод сохранения задач в файл
    public void save() {
        List<Task> allTasksToSave = new ArrayList<>();
        allTasksToSave.addAll(getAllTasks());
        allTasksToSave.addAll(getAllEpics());
        allTasksToSave.addAll(getAllSubTasks());

        try (Writer fileWriter = new FileWriter(file)) {
            for (Task task : allTasksToSave) {
                String taskAsString = toString(task);
                fileWriter.write(taskAsString);
            }
        } catch (IOException e) {
            throw new FileBackedTaskManagerOutputException("Произошла ошибка при записи задач в файл!", e);
        }

        List<Task> history = new ArrayList<>(getHistory());
        try (Writer fileWriter = new FileWriter(file, true)) {
            fileWriter.write("\n");
            for (Task task : history) {
                String taskFromHistoryAsString = toString(task);
                fileWriter.write(taskFromHistoryAsString);
            }
        } catch (IOException e) {
            throw new FileBackedTaskManagerOutputException("Произошла ошибка при сохранении истории в файл!");
        }
    }

    // Метод создания задачи из строки
    public static Task fromString(String value) {
        String[] elements = value.split(",");
        int taskId = Integer.parseInt(elements[0]);
        Status status = Status.valueOf(elements[2]);
        String name = elements[3];
        String description = elements[4];

        switch (elements[1]) {
            case "SUBTASK":
                int epicId = Integer.parseInt(elements[5]);
                return new SubTask(epicId, taskId, name, description, status);
            case "EPIC":
                Epic epic = new Epic(taskId, name, description);
                epic.setStatus(status);
                if (elements.length == 6) { // если у эпика существуют подзадачи
                    elements[5] = elements[5].substring(2);
                    String[] subTasks = elements[5].split("st");
                    for (String string : subTasks) {
                        epic.getSubTasksIdList().add(Integer.parseInt(string));
                    }
                }
                return epic;
            default:
                return new Task(taskId, name, description, status);
        }
    }

    // Метод восстановления менеджера из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        boolean isHistory = false;
        List<Task> historyList = new ArrayList<>();
        // восстанавливаем задачи
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String taskAsLine = bufferedReader.readLine();
                // достигли конца сохраненных задач, дальше - история
                if (taskAsLine.isBlank() || taskAsLine.isEmpty()) {
                    isHistory = true;
                    continue;
                }
                Task newTask = fromString(taskAsLine);
                if (!isHistory) {
                    if (newTask instanceof SubTask) {
                        manager.allSubTasks.put(newTask.getId(), (SubTask) newTask);
                    } else if (newTask instanceof Epic) {
                        Epic epic = (Epic) newTask;
                        manager.allEpics.put(epic.getId(), epic);
                    } else {
                        manager.allTasks.put(newTask.getId(), newTask);
                    }
                } else {
                    historyList.add(newTask);
                }
            }
            // Восстанавливаем историю
            for (Task task : historyList) {
                manager.historyManager.add(task);
            }
            // Восстанавливаем значение счетчика задач
            manager.setTaskCounter(manager.allTasks.size() + manager.allEpics.size() + manager.allSubTasks.size());
        } catch (IOException e) {
            throw new FileBackedTaskManagerInputException("Произошла ошибка при восстановлении задач из файла!");
        }
        return manager;
    }

}
