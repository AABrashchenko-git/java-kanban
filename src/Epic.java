import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    static int subTaskId;
    // ArrayList<SubTask> subTasks = new ArrayList<>();
    HashMap<Integer, SubTask> subTasks;

    public Epic(String name, String description, Progress progress) {
        super(name, description, progress);
        subTasks = new HashMap<>();
    }

    public void addSubTask(SubTask subTask) {

        subTasks.put(subTaskId, subTask);
        subTaskId ++;
    }

    @Override
    public String toString() {
        return "Эпик: {" +
                "имя эпика: '" + name + '\'' +
                ", описание: '" + description + '\'' +
                ", статус: " + progress +
                '}' + "Текущие подзадачи: " + subTasks;
    }


}
