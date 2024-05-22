import java.util.Objects;

public class SubTask extends Task{

    String epicName;
    int epicId;

    public SubTask(String epicName, String name, String description, Progress progress) {
        super(name, description, progress);
        this.epicName = epicName;
    }


    @Override
    public int getId() {
        epicId = Objects.hash(this.epicName);
        return epicId;
    }

    @Override
    public String toString() {

/*        return "Эпик: " + (this.getId()) + "Подзадача: {" +
                "Имя подзадачи:'" + name + '\'' +
                ", описание: " + description + '\'' +
                ", статус: " + progress +
                '}';*/


        return "Эпик: " + (this.epicName) + ". Имя подзадачи:'" + name + '\'' +
                ", описание: " + description + '\'' +
                ", статус: " + progress;
    }
}
