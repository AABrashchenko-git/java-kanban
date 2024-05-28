import java.util.*;

public class TaskManager {
    private static int taskCounter;
    private static HashMap<Integer, Task> allTasks = new HashMap<>();
    private static HashMap<Integer, Epic> allEpics = new HashMap<>();


    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // d. Создание. Сам объект должен передаваться в качестве параметра.
    // 1) Задачи
    public static void addTask(Task task) {
        //TODO проверку на null надо?
        task.setId(getNextId());
        allTasks.put(task.getId(), task);
    }

    // 2) Эпики
    public static void addEpic(Epic epic) {
        //TODO проверку на null надо?
        epic.setId(getNextId());
        allEpics.put(epic.getId(), epic);
    }

    // 3) Подзадачи
    public static void addSubTask(SubTask subTask) {
        //TODO проверку на null надо?
        subTask.setId(getNextId());
        int epicId = subTask.getEpicId();
        Epic epic = allEpics.get(epicId);
        epic.addSubTask(subTask);
        epic.setStatus(getUpdatedEpicStatus(epic));
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    // 1) Задачи
    public static Task updateTask(Task task) {
        //TODO проверку на null надо
        return allTasks.replace(task.getId(), task);
    }

    // 2) Эпики
    public static Epic updateEpic(Epic epic) {
        //TODO проверку на null надо
        return allEpics.replace(epic.getId(), epic);
    }

    // 3) Подзадачи
    public static SubTask updateSubTask(SubTask subTask) {
        //TODO проверку на null надо
        Epic epicOfSubTask = allEpics.get(subTask.getEpicId());

        epicOfSubTask.subTasks.replace(subTask.getId(), subTask);
        epicOfSubTask.setStatus(getUpdatedEpicStatus(epicOfSubTask));
        return subTask;
    }

    // c. Получение по идентификатору.
    // 1) Задачи
    public static Task getTaskById(int taskId) {
        return allTasks.get(taskId);
    }

    // 2) Эпики
    public static Epic getEpicById(int epicId) {
        return allEpics.get(epicId);
    }

    // 3) Подзадачи
    public static SubTask getSubTaskById(int subTaskId) {
        SubTask subTaskFound = null;
        for (Epic epic : allEpics.values()) {
            if (epic.subTasks.containsKey(subTaskId)) {
                subTaskFound = epic.subTasks.get(subTaskId);
            }
        }
        return subTaskFound;
    }

    // a. Получение списка всех задач.
    // 1) Задачи
    public static ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>(allTasks.values());
        return tasks;
    }

    // 2) Эпики
    public static ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epics = new ArrayList<>(allEpics.values());
        return epics;
    }

    // 3) Подзадачи
    public static ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (Epic epic : allEpics.values()) {
            subTasks.addAll(epic.subTasks.values());
        }
        return subTasks;
    }

    // b. Удаление всех задач.
    // 1) Задачи
    public static void removeTasks() {
        allTasks.clear();
    }

    // 2) Эпики
    public static void removeEpics() {
        for (Epic epic : allEpics.values()) {
            epic.subTasks.clear();
        }
        allEpics.clear();
    }

    // 3) Подзадачи
    public static void removeSubTasks() {
        for (Epic epic : allEpics.values()) {
            epic.subTasks.clear();
        }
    }

    // f. Удаление по идентификатору.
    // 1) Задачи
    public static void removeOneTaskById(int taskId) {
        allTasks.remove(taskId);
    }

    // 2) Эпики
    public static void removeOneEpicById(int epicId) {
        Epic epic = getEpicById(epicId);
        epic.subTasks.clear();
        allEpics.remove(epicId);
    }

    // 3) Подзадачи
    public static void removeOneSubTaskById(int subTaskId) {
        SubTask subTask = getSubTaskById(subTaskId);
        Epic epic = getEpicById(subTask.getEpicId());
        epic.subTasks.remove(subTask.getId());
        epic.setStatus(getUpdatedEpicStatus(epic));
    }
    //Дополнительные методы:

    //a. Получение списка всех подзадач определённого эпика.
    public static ArrayList<SubTask> getAllSubTaskOfEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        ArrayList<SubTask> subTasks = new ArrayList<>(epic.subTasks.values());
        return subTasks;
    }

    public static int getNextId() {
        int nextId = ++taskCounter;
        return nextId;
    }

    public static Status getUpdatedEpicStatus(Epic epic) {
        int newCount = 0;
        int doneCount = 0;
        int inProgressCount = 0;

        if (epic.subTasks.isEmpty()) {
            return Status.NEW;
        }
        for (SubTask subTask : epic.subTasks.values()) {
            if (subTask.getStatus() == Status.NEW) {
                newCount++;
            } else if (subTask.getStatus() == Status.DONE) {
                doneCount++;
            } else {
                inProgressCount++;
            }
        }
        if (newCount > 0 && doneCount == 0 && inProgressCount == 0) {
            return Status.NEW;
        } else if (inProgressCount == 0 && newCount == 0 && doneCount > 0) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

}
