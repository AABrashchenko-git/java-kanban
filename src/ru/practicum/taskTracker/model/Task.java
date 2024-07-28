package ru.practicum.taskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

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

    // Конструктор для создания задачи с временными метками
    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.status = Status.NEW;
    }

    // Конструктор для обновления задачи с временными метками
    public Task(int id, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isBlank() && !name.isEmpty()) {
            this.name = name;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && !description.isBlank() && !description.isEmpty()) {
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

    public Type getType() {
        return Type.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) return null;
        return startTime.withSecond(0).withNano(0); // чтобы не сравнивать секунды и наносекунды
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null)
            return null;
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if ((this.startTime == null || task.getStartTime() == null))
            return id == task.id;
        return id == task.id && Objects.equals(startTime.withSecond(0).withNano(0),
                task.startTime.withSecond(0).withNano(0));
    }

    @Override
    public int hashCode() {
        if (this.startTime != null)
            return Objects.hash(id, startTime.withSecond(0).withNano(0));
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        StringBuilder taskInfo = new StringBuilder();
        taskInfo.append("\b\b\n [ID задачи: ");
        taskInfo.append(this.getId());
        taskInfo.append("; Имя задачи: ");
        taskInfo.append(this.getName());
        taskInfo.append(", описание задачи: ");
        taskInfo.append(this.getDescription());
        taskInfo.append(", текущий статус задачи: ");
        taskInfo.append(this.getStatus());
        if (this.startTime != null && this.duration != null) {
            taskInfo.append(", время начала задачи: ").append(this.getStartTime().format(formatter))
                    .append(", время окончания задачи: ").append(this.getEndTime().format(formatter));
        }
        taskInfo.append("]");
        return taskInfo.toString();
    }
}
