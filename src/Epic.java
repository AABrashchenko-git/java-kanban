import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    // String taskType = "epic";
    static int subTaskId;
    // ArrayList<SubTask> subTasks = new ArrayList<>();
    HashMap<Integer, SubTask> subTasks;

    public Epic(String name, String description, Progress progress) {
        super(name, description, progress);
        subTasks = new HashMap<>();
        taskType = "epic";
    }

    public void addSubTask(SubTask subTask) {

        subTasks.put(subTaskId, subTask);
        subTaskId ++;
    }

    @Override
    public String toString() {
        int i = 1;
        String result = "";
        for(SubTask subTask : subTasks.values()) {
           result = result + "\n" + i + ". " + subTask.name + ". Описание: " + subTask.description + ". Статус: " + subTask.getProgress() + ";";
           i++;
        }



        return "Имя эпика: '" + name + '\'' +
                 ". Текущие подзадачи: " + result;


    }


}
