package ru.practicum.taskTracker.service;

import ru.practicum.taskTracker.model.Epic;
import ru.practicum.taskTracker.model.SubTask;
import ru.practicum.taskTracker.model.Task;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    // private File taskStorage = new File(System.getProperty("user.dir")+"\\taskStorage\\tasks.csv");
    private final String WORKING_DIRECTORY = System.getProperty("user.dir")+"\\taskStorage";
    Path taskStorage = Paths.get(WORKING_DIRECTORY, "taskStorage", "tasks.csv");

    public FileBackedTaskManager(Path taskStorage) {
        this.taskStorage = taskStorage;
    }

    public static void save() {

    }

    static FileBackedTaskManager loadFromFile(File file) {
        return null;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        ////////
        return task; //TODO вернем задачу, или перепишем контракт родителя?
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        ////////
        return epic;//TODO вернем задачу, или перепишем контракт родителя?
    }

    @Override
    public SubTask addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        ////////
        return subtask;//TODO вернем задачу, или перепишем контракт родителя?
    }

    // ОСТАЛЬНЫЕ МЕТОДЫ ПЕРЕОПРЕДЕЛИТЬ..........


    //метод создания задачи из строки
    public Task fromString(String value) {
        //  Files.readString(file.toPath());
        return null;
    }
    //метод сохранения задачи в строку
    @Override
    public String toString() {
        return super.toString();
    }

}
