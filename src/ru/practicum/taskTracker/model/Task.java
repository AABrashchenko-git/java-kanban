package ru.practicum.taskTracker.model;

import ru.practicum.taskTracker.service.InMemoryTaskManager;

import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id >= InMemoryTaskManager.getTaskCounter()) {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Нашел такую штуку, чтобы вводимая строка соответствовала регулярному
        // выражению (строки, пробелы, символы и тд), если есть другие подходы - буду признателен, если поделишься :)
        if (name != null && (name.length() > 1) && name.matches("^[\\p{L}\\p{N}\\s\\p{P}]+$")) {
            this.name = name;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && (description.length() > 1)
                && description.matches("^[\\p{L}\\p{N}\\s\\p{P}]+$")) {
            this.description = description;
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if (status != null) {
            this.status = status;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder taskInfo = new StringBuilder();
        taskInfo.append("\b\b\n [ID задачи: ");
        taskInfo.append(this.getId());
        taskInfo.append("; Имя задачи: ");
        taskInfo.append(this.getName());
        taskInfo.append(", описание задачи: ");
        taskInfo.append(this.getDescription());
        taskInfo.append(", текущий статус задачи: ");
        taskInfo.append(this.getStatus());
        taskInfo.append("]");
        return taskInfo.toString();
    }
}
