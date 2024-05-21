import java.util.ArrayList;

public class Epic extends Task {
    static int epicId;
    ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description, Progress progress) {
        super(name, description, progress);
    }

    public void addNewSubTask(SubTask subTask) {
        subTasks.add(subTask);
        epicId++;
    }
    @Override
    public String toString() {
        return "Epic{" +
                /*"subTasks=" + subTasks +*/
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", progress=" + progress +
                '}';
    }
   /* public Epic() {
        // эпик с подзадачами
    }*/
}
