package ru.practicum.taskTracker.model;

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
        subTaskInfo.append(this.getStatus()).append("]");

        return subTaskInfo.toString();
    }
}