import java.util.Objects;

public class Task {
    private static int id;
    private String name;
    private String description;
    private Progress progress;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.progress = Progress.NEW;
    }
    public int getId() {
        id = Objects.hash(this.name);
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
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
        return Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Имя задачи: '" + name + '\'' +
                ", описание задачи: '" + description + '\'' +
                ", текущий статус задачи: " + progress;
    }
}
