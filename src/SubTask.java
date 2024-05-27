import java.util.Objects;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(int epicId, String name, String description) {
        super(name, description);
        this.epicId = epicId;
        this.setStatus(Status.NEW);
     }

    public SubTask(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.epicId = getEpicId();
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Эпик: " + /*(this.epicName) +*/ ". Имя подзадачи:'" + this.getName() + '\'' +
                ", описание: " + this.getDescription() + '\'' +
                ", статус: " + this.getStatus();
    }



}
