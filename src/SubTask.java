import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(int epicId, String name, String description) {
        super(name, description);
        this.epicId = epicId;
        this.setStatus(Status.NEW);
     }

    public SubTask(int epicId, int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        StringBuilder subTaskInfo = new StringBuilder();
        subTaskInfo.append("\n[Эпик: ").append(TaskManager.getEpicById(getEpicId()).getName()).append(", ID эпика: ");
        subTaskInfo.append(this.getEpicId()).append("; ID подзадачи: ").append(this.getId()).append("; Имя подзадачи: ");
        subTaskInfo.append(this.getName()).append(", описание подзадачи: ").append(this.getDescription());
        subTaskInfo.append(", статус подзадачи: ").append(this.getStatus()).append("]");

        return subTaskInfo.toString();
    }



}
