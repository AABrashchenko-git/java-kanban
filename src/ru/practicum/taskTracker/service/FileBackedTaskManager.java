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

    public static void main(String[] args) {
        // Пользовательский сценарий
        File taskStorage = new File(System.getProperty("user.dir") + "\\taskStorage\\tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(taskStorage);

        // 1. Заведите несколько разных задач, эпиков и подзадач
        Task task1 = new Task("имяЗадачи1", "описаниеЗадачи1");
        Task task2 = new Task("имяЗадачи2", "описаниеЗадачи2");
        manager.addTask(task1);
        manager.addTask(task2);
        Epic epic1 = new Epic("имяЭпика1", "описаниеЭпика1");
        Epic epic2 = new Epic("имяЭпика2", "описаниеЭпика2");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        SubTask subTask1 = new SubTask(epic1.getId(), "имяПодзадачи1Эпика1",
                "описаниеПодзадачи1Эпика1");
        SubTask subTask2 = new SubTask(epic1.getId(), "имяПодзадачи2Эпика1",
                "описаниеПодзадачи2Эпика1");
        SubTask subTask3 = new SubTask(epic1.getId(), "имяПодзадачи3Эпика1",
                "описаниеПодзадачи1Эпика2");
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);

        // Получим задачи, чтобы добавить их в историю
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getTaskById(task1.getId());
        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask1.getId());
        // Обновим одну подзадачу, чтобы после восстановления истории увидеть, что её статус(и её эпика) также сохранились
        manager.updateSubTask(new SubTask(subTask1.getEpicId(), subTask1.getId(), "newИмяПодзадачи1Эпика1",
                "newОписаниеПодзадачи1Эпика1", Status.DONE));
        manager.getSubTaskById(subTask1.getId());

        // 2. Создайте новый FileBackedTaskManager-менеджер из этого же файла.
        FileBackedTaskManager newManager = loadFromFile(taskStorage);

        // 3. Проверьте, что все задачи, эпики, подзадачи, которые были в старом менеджере, есть в новом
        System.out.println("\nИсходный список задач");
        System.out.println("___________________________________");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
        System.out.println("\nСписок задач, восстановленный из файла:");
        System.out.println("___________________________________");
        System.out.println(newManager.getAllTasks());
        System.out.println(newManager.getAllEpics());
        System.out.println(newManager.getAllSubTasks());

        System.out.println("\nИсходная история:");
        System.out.println("___________________________________");
        System.out.println(manager.getHistory());
        System.out.println("\nИстория, восстановленная из файла:");
        System.out.println("___________________________________");
        System.out.println(newManager.getHistory());

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
        if (task instanceof SubTask) {
            taskToString.append(",").append(((SubTask) task).getEpicId());
        } else if (task instanceof Epic) {
            taskToString.append(",");
            for (Integer subTaskId : ((Epic) task).getSubTasksIdList()) {
                taskToString.append("st").append(subTaskId);
            }
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
            throw new FileBackedTaskManagerOutputException("Произошла ошибка при записи задач в файл!");
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
