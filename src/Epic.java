import java.util.HashMap;

public class Epic extends Task {
    HashMap<Integer, SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new HashMap<>();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getSubTaskId(), subTask);
    }

    @Override
    public String toString() {
        int i = 1;
        String result = "";
        for (SubTask subTask : subTasks.values()) {
            result = result + "\n" + i + ". " + subTask.getName() + ". Описание: " + subTask.getDescription() + ". Статус: " + subTask.getProgress() + ";";
            i++;
        }
        return "Имя эпика: " + this.getName() + ". Описание: " + this.getDescription() + ". Статус: " + this.getProgress() +
                ". Текущие подзадачи: " + result;
    }
}
