package ru.practicum.taskTracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<SubTask> subTaskArrayList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        this.setStatus(Status.NEW);
    }

    public Epic(int id, String name, String description) {
        super(name, description);
        this.setId(id);
    }

    public ArrayList<SubTask> getSubTasksList() {
        return subTaskArrayList;
    }

    public void addSubTaskToList(SubTask subTask) {
        subTaskArrayList.add(subTask);
    }

    public void replaceElementInSubTaskList(SubTask subTask) {
        for (SubTask subTaskFromList : subTaskArrayList) {
            if (subTask.getId() == subTaskFromList.getId()) {
                int index = subTaskArrayList.indexOf(subTaskFromList);
                subTaskArrayList.set(index, subTask);
            }
        }
    }

    @Override
    public String toString() {
        int i = 1;
        StringBuilder subTaskInfo = new StringBuilder();
        StringBuilder epicInfo = new StringBuilder();
        if (subTaskArrayList.isEmpty()) {
            subTaskInfo.append("\n\t -- Список подзадач на данный момент пуст или удалён.");
        } else {
            for (SubTask subTask : this.getSubTasksList()) {
                subTaskInfo.append("\n\t");
                subTaskInfo.append(i);
                subTaskInfo.append(". ID подзадачи: ");
                subTaskInfo.append(subTask.getId());
                subTaskInfo.append(". Имя подзадачи: ");
                subTaskInfo.append(subTask.getName());
                subTaskInfo.append(". Описание: ");
                subTaskInfo.append(subTask.getDescription());
                subTaskInfo.append(". Статус: ");
                subTaskInfo.append(subTask.getStatus());
                i++;
            }
        }
        epicInfo.append("\b\b\n [ID эпика: ").append(this.getId()).append("; Имя эпика: ").append(this.getName());
        epicInfo.append(". Описание: ").append(this.getDescription()).append(". Текущий статус: ").append(this.getStatus());
        epicInfo.append(". Текущие подзадачи: ").append(subTaskInfo).append("]");
        return epicInfo.toString();
    }
}
