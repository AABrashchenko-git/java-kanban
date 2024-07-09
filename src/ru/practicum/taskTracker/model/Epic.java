package ru.practicum.taskTracker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTasksIdList = new ArrayList<>();
    private final Type type = Type.EPIC;

    public Epic(String name, String description) {
        super(name, description);
        this.setStatus(Status.NEW);
    }

    public Epic(int id, String name, String description) {
        super(name, description);
        this.setId(id);
    }

    public List<Integer> getSubTasksIdList() {
        return subTasksIdList;
    }

    public void addSubTaskIdToList(int subTaskId) {
        subTasksIdList.add(subTaskId);
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
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
        epicInfo.append(". Описание: ").append(this.getDescription()).append(". Текущий статус: ").append(this.getStatus());
        epicInfo.append(". ID текущих подзадач: ").append(subTaskInfo).append("]");
        return epicInfo.toString();
    }
}
