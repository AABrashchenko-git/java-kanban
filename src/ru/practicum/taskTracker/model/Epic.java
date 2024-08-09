package ru.practicum.taskTracker.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksIdList = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.setStatus(Status.NEW);
    }

    public Epic(int id, String name, String description) {
        super(name, description);
        this.setId(id);
        subTasksIdList = new ArrayList<>();
    }
    public List<Integer> getSubTasksIdList() {
        return subTasksIdList;
    }
    public void addSubTaskIdToList(int subTaskId) {
        subTasksIdList.add(subTaskId);
    }

    public Type getType() {
        return Type.EPIC;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        StringBuilder subTaskInfo = new StringBuilder();
        StringBuilder epicInfo = new StringBuilder();
        if (subTasksIdList.isEmpty()) {
            subTaskInfo.append("--> Список подзадач на данный момент пуст или удалён.");
        } else {
            for (Integer id : this.getSubTasksIdList()) {
                subTaskInfo.append(id).append(",");
            }
        }
        epicInfo.append("\b\b\n [ID эпика: ").append(this.getId()).append("; Имя эпика: ").append(this.getName());
        epicInfo.append(". Описание: ").append(this.getDescription());
        epicInfo.append(". Текущий статус: ").append(this.getStatus());
        if (this.getStartTime() != null && this.getDuration() != null) {
            epicInfo.append(", время начала эпика: ").append(this.getStartTime().format(formatter))
                    .append(", время окончания эпика: ").append(this.getEndTime().format(formatter));
        }
        epicInfo.append(". ID текущих подзадач: ").append(subTaskInfo).append("]");
        return epicInfo.toString();
    }

}
