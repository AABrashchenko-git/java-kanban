import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Task {
    public static int id;
    protected String name;
    protected String description;

    protected Progress progress;

    public Task(String name, String description, Progress progress) {
        this.name = name;
        this.description = description;
        this.progress = progress;
    }


    public int getId() {
        id = Objects.hash(this.name);
        return id;
    }

    public static void setId(int id) {
        Task.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Задача: {" +
                "Имя задачи: '" + name + '\'' +
                ", описание задачи: '" + description + '\'' +
                ", текущий статус задачи: " + progress +
                '}';
    }
}
