import java.util.Objects;

public class SubTask extends Task {
    // String taskType = "subTask";
    int subTaskId;
    String epicName;
    int epicId;

    public SubTask(String epicName, String name, String description, Progress progress) {
        super(name, description, progress);
        this.epicName = epicName;
        taskType = "subTask";
    }

    public SubTask(String epicName, String name, String description) {
        super(name, description);
        this.progress = Progress.NEW; //TODO ИСПРАВИТЬ
        this.epicName = epicName;
        taskType = "subTask";
    }

    @Override
    public int getId() {
        epicId = Objects.hash(this.epicName);
        return epicId;
    }

    public int getEpicId() {
        epicId = Objects.hash(this.epicName);
        return epicId;
    }

    public int getSubTaskId() {
        subTaskId = Objects.hash(this.name);
        return subTaskId;
    }

    @Override
    public String toString() {
        return "Эпик: " + (this.epicName) + ". Имя подзадачи:'" + name + '\'' +
                ", описание: " + description + '\'' +
                ", статус: " + progress;
    }
}
