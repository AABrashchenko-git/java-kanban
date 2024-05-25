import java.util.Objects;

public class SubTask extends Task {
    protected int subTaskId;
    protected String epicName;
    protected int epicId;

    public SubTask(String epicName, String name, String description) {
        super(name, description);
        this.setProgress(Progress.NEW);
        this.epicName = epicName;
    }

    public int getEpicId() {
        epicId = Objects.hash(this.epicName);
        return epicId;
    }

    public int getSubTaskId() {
        subTaskId = Objects.hash(this.getName());
        return subTaskId;
    }

    @Override
    public String toString() {
        return "Эпик: " + (this.epicName) + ". Имя подзадачи:'" + this.getName() + '\'' +
                ", описание: " + this.getDescription() + '\'' +
                ", статус: " + this.getProgress();
    }
}
