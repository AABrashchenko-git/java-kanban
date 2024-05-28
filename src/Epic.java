import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, SubTask> subTasks = new HashMap<>();;

    public Epic(String name, String description) {
        super(name, description);
        this.setStatus(Status.NEW);
    }

    public Epic(int id, String name, String description) {
        super(name, description);
        this.setId(id);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public String toString() {
        int i = 1;
        StringBuilder subTaskInfo = new StringBuilder();
        StringBuilder epicInfo = new StringBuilder();
        for (SubTask subTask : subTasks.values()) {
            subTaskInfo.append("\n\t").append(i).append(". ID подзадачи: ").append(subTask.getId());
            subTaskInfo.append( ". Имя подзадачи: ").append(subTask.getName());
            subTaskInfo.append( ". Описание: ").append(subTask.getDescription()).append(". Статус: ");
            subTaskInfo.append(subTask.getStatus());
            i++;
        }
        epicInfo.append(" \n[ID эпика: ").append(this.getId()).append("; Имя эпика: ").append(this.getName());
        epicInfo.append(". Описание: ").append(this.getDescription()).append( ". Статус: ").append(this.getStatus());
        epicInfo.append(". Текущие подзадачи: ").append(subTaskInfo).append("]");
        return epicInfo.toString();
    }
}
