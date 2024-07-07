package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    // private File taskStorage = new File(System.getProperty("user.dir")+"\\taskStorage\\tasks.csv");
    //  private final String WORKING_DIRECTORY = System.getProperty("user.dir") + "\\taskStorage";
    //  Path taskStorage = Paths.get(WORKING_DIRECTORY, "taskStorage", "tasks.csv");
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Сохранение задач в файл
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
            throw new RuntimeException(e);
        }

        List<Task> history = new ArrayList<>(getHistory());
        try (Writer fileWriter = new FileWriter(file, true)) {
            fileWriter.write("\n");
            for (Task task : history) {
                String taskFromHistoryAsString = toString(task);
                fileWriter.write(taskFromHistoryAsString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO бросить собственное исключение
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        //  Files.readString(file.toPath());
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        boolean isHistory = false;
        // восстанавливаем задачи
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String taskAsLine = bufferedReader.readLine();
                Task newTask = fromString(taskAsLine);
                if (isHistory==false) {
                    if (newTask instanceof SubTask) {
                        manager.addSubTask((SubTask) newTask);
                    } else if (newTask instanceof Epic) {
                        Epic epic = (Epic) newTask;
                        manager.addEpic(epic);
                    } else {
                        manager.addTask(newTask);
                    }
                    // достигли конца сохраненных задач, меняем isHistory=true, продолжаем цикл со след строки
                    if (taskAsLine.isBlank() || taskAsLine.isEmpty()) {
                        isHistory = true;
                        continue;
                    }
                } else {
                    manager.getHistory().add(newTask);
                }
            }
            // восстанавливаем подзадачи внутри объектов эпика по его id //TODO не надо ли это наружу while?
            for (Epic epic : manager.getAllEpics()) {
                for (Integer subTaskId : epic.getSubTasksIds()) {
                    SubTask subTask = manager.getSubTaskById(subTaskId);
                    epic.addSubTaskToList(subTask);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return manager;
    }

    //метод создания задачи из строки
    public static Task fromString(String value) {
        //TODO бросить исключение, если тут какая-то херь происходит?
        String[] elements = value.split(",");
        //TODO выбросить ошибку рантайм, которую обработает вызывающий метод
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
                String[] subTasks = elements[5].split("st");
                for (String string : subTasks) {
                    epic.getSubTasksIds().add(Integer.parseInt(string));
                }
                return epic;
            default:
                return new Task(taskId, name, description, status);
        }
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

    // 3) Подзадачи
    @Override
    public void removeSubTasks() {
        super.removeSubTasks();
        save();
    }

    //метод сохранения задачи в строку
    public String toString(Task task) {
        String taskToString = String.format("%s,%s,%s,%s,%s", task.getId(), task.getType().name(), task.getStatus(),
                task.getName(), task.getDescription());
        StringBuilder taskToString1 = new StringBuilder(taskToString);
        if (task instanceof SubTask) {
            return taskToString + "," + ((SubTask) task).getEpicId() + "\n";
        } else if (task instanceof Epic) {
            for (SubTask subTask : ((Epic) task).getSubTasksList()) {
                taskToString1.append(",");
                taskToString1.append("st").append(subTask.getId());
            }
            return taskToString1.toString() + "\n";
        } else {
            return taskToString + "\n";
        }
    }

}
