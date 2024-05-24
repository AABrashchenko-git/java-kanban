import java.util.Objects;

public class SubTask extends Task {
    protected int subTaskId;
    protected String epicName;
    protected int epicId;

    public SubTask(String epicName, String name, String description) {
        super(name, description);
        this.progress = Progress.NEW;
        this.epicName = epicName;
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
