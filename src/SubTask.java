import java.util.Objects;

public class SubTask extends Task{
   // String taskType = "subTask";
    String epicName;
    int epicId;

    public SubTask(String epicName, String name, String description, Progress progress) {
        super(name, description, progress);
        this.epicName = epicName;
        taskType = "subTask";
    }


    @Override
    public int getId() {
        epicId = Objects.hash(this.epicName);
        return epicId;
    }

    @Override
    public String toString() {
        return "Эпик: " + (this.epicName) + ". Имя подзадачи:'" + name + '\'' +
                ", описание: " + description + '\'' +
                ", статус: " + progress;
    }
}
