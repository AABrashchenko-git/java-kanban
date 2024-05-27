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
        String result = "";
        for (SubTask subTask : subTasks.values()) {
            result = result + "\n" + i + ". " + subTask.getName() + ". Описание: " + subTask.getDescription() + ". Статус: " + subTask.getStatus() + ";";
            i++;
        }
        return "Имя эпика: " + this.getName() + ". Описание: " + this.getDescription() + ". Статус: " + this.getStatus() +
                ". Текущие подзадачи: " + result;
    }
}
