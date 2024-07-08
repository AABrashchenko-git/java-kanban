package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File taskStorage = new File(System.getProperty("user.dir") + "\\taskStorage\\tasks.csv");
        /*FileBackedTaskManager manager = new FileBackedTaskManager(taskStorage);


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

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubTaskById(subTask2.getId());
        manager.getSubTaskById(subTask3.getId());
        manager.getTaskById(task1.getId());
        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask2.getId());*/


        FileBackedTaskManager newManager = loadFromFile(taskStorage);

        /*System.out.println("ПЕРВЫЙ МЕНЕДЖЕР");
        System.out.println("___________________________________");
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
        System.out.println("История:");
        System.out.println(manager.getHistory());*/

        System.out.println("МЕНЕДЖЕР ИЗ ФАЙЛА");
        System.out.println("___________________________________");
        System.out.println(newManager.getAllTasks());
        System.out.println(newManager.getAllEpics());
        System.out.println(newManager.getAllSubTasks());
        System.out.println("История:");
        System.out.println(newManager.getHistory());
        System.out.println(newManager.getAllEpics().getFirst().getStatus());
        System.out.println(newManager.getAllSubTasks().getFirst().getStatus());

        Task taskTest = new Task("naaaaaaaaaaame", "descriiiiiiiiiiption");
        newManager.addTask(taskTest);
        System.out.println(taskTest);
        newManager.getTaskById(taskTest.getId());
        System.out.println(newManager.getHistory());
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
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        boolean isHistory = false;
        List<Integer> historyIds = new ArrayList<>();
        List<Task> historyList = new ArrayList<>();
        // восстанавливаем задачи
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String taskAsLine = bufferedReader.readLine();
                // достигли конца сохраненных задач, меняем isHistory=true, продолжаем цикл со след строки
                if (taskAsLine.isBlank() || taskAsLine.isEmpty() || !String.valueOf(taskAsLine.charAt(0)).matches("\\d+")) {
                    isHistory = true;
                    continue;
                }
                Task newTask = fromString(taskAsLine);
                if (isHistory == false) {
                    if (newTask instanceof SubTask) {
                        manager.allSubTasks.put(newTask.getId(), (SubTask) newTask);
                    } else if (newTask instanceof Epic) {
                        Epic epic = (Epic) newTask;
                        /*manager.addEpic(epic);*/
                        manager.allEpics.put(epic.getId(), epic);

                    } else {
                        // manager.addTask(newTask);
                        manager.allTasks.put(newTask.getId(), newTask);
                    }
                } else { // восстанавливаем историю //TODO ниже восстанавливаем? разобраться, что написал
                    historyList.add(newTask);
                }

            }
            // Восстанавливаем историю:
            for (Task task : historyList) {
                if (manager.allTasks.containsKey(task.getId())) {
                    manager.historyManager.add(manager.allTasks.get(task.getId()));
                } else if (manager.allEpics.containsKey(task.getId())) {
                    manager.historyManager.add(manager.allEpics.get(task.getId()));
                } else if (manager.allSubTasks.containsKey(task.getId())) {
                    manager.historyManager.add(manager.allSubTasks.get(task.getId()));
                }
            }
            // Восстанавливаем статусы эпиков
            for (Epic epic : manager.getAllEpics()) {
                epic.setStatus(manager.getUpdatedEpicStatus(epic));
            }
            // Восстанавливаем значение счетчика задач
            manager.setTaskCounter(manager.allTasks.size() + manager.allEpics.size() + manager.allSubTasks.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return manager;
    }

    //метод создания задачи из строки
    public static Task fromString(String value) {
        //TODO бросить исключение, если тут происходит ошибка?
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

    @Override
    public void addToHistory(Task task) {
        super.addToHistory(task);
        save();
    }

    //метод сохранения задачи в строку
    public String toString(Task task) {
        String taskToString = String.format("%s,%s,%s,%s,%s", task.getId(), task.getType().name(), task.getStatus(), //TODO надо ли писать для типа name()?
                task.getName(), task.getDescription());
        StringBuilder taskToString1 = new StringBuilder(taskToString);
        if (task instanceof SubTask) {
            return taskToString + "," + ((SubTask) task).getEpicId() + "\n";
        } else if (task instanceof Epic) {
            taskToString1.append(",");
            for (Integer subTaskId : ((Epic) task).getSubTasksIdList()) {
                taskToString1.append("st").append(subTaskId);
            }
            return taskToString1.toString() + "\n";
        } else {
            return taskToString + "\n";
        }
    }

}
