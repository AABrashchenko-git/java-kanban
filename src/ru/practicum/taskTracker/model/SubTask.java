package ru.practicum.taskTracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;
    private final Type type = Type.SUBTASK;

    public SubTask(int epicId, String name, String description) {
        super(name, description);
        this.setEpicId(epicId);
        this.setStatus(Status.NEW);
    }

    public SubTask(int epicId, int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.setEpicId(epicId);
    }

    public SubTask(int epicId, String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.setEpicId(epicId);
        this.setStatus(Status.NEW);
    }

    public SubTask(int epicId, int id, String name, String description, Status status, LocalDateTime startTime,
                   Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.setEpicId(epicId);
    }


    public int getEpicId() {
        return epicId;
    }

    private void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder subTaskInfo = new StringBuilder();
        subTaskInfo.append("\b\b\n [ID подзадачи: ");
        subTaskInfo.append(this.getId());
        subTaskInfo.append("; ID эпика: ");
        subTaskInfo.append(this.getEpicId());
        subTaskInfo.append("; Имя подзадачи: ");
        subTaskInfo.append(this.getName());
        subTaskInfo.append(", описание подзадачи: ");
        subTaskInfo.append(this.getDescription());
        subTaskInfo.append(", статус подзадачи: ");
        subTaskInfo.append(this.getStatus());
        if (this.getStartTime() != null && this.getDuration() != null) {
            subTaskInfo.append(", время начала подзадачи: ").append(this.getStartTime())
                    .append(", время окончания подзадачи: ").append(this.getEndTime());
        }
        subTaskInfo.append("]");

        return subTaskInfo.toString();
    }
}