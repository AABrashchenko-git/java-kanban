import java.util.*;

public class TaskManager {
    public static HashMap<Integer, Task> allTasks = new HashMap<>();

    // 1. Создание задачи (добавление в список). Сам объект передается в качестве параметра
    // Добавление  задачи
    public static void addNewTask(Task task) {
        allTasks.put(task.getId(), task);
    }

    //  Добавление  эпика
    public static void addNewEpic(Epic epic) {
        allTasks.put(epic.getId(), epic);
    }

    //  Добавление  подзадачи (сама подзадача при создании уже содержит информацию о эпике, которому она принадлежит)
    public static void addNewSubTask(SubTask subTask) {
        for (Task task : allTasks.values()) {
            if (task.getId() == subTask.getEpicId()) {
                Epic epic = (Epic) task;
                epic.addSubTask(subTask);
            }
        }
    }

    // 2. Получение списка каждого типа задач

    //  Получение списка всех задач
    public static void printAllTasks() {
        int i = 1;
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("task")) {
                System.out.printf(i++ + ". " + task.getName() + "\n");
            }
        }
    }

    //  Получение списка всех эпиков
    public static void printAllEpics() {
        int i = 1;
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("epic")) {
                System.out.printf(i++ + ". " + task.getName() + "\n");
            }
        }
    }

    //  Получение списка всех подзадач конкретного эпика
    public static void printAllSubTask(String epicName) {
        for (Task task : allTasks.values()) {
            if (task.hashCode() == Objects.hash(epicName)) {
                System.out.println(task);
            }
        }
    }

    // 3. Удаление всех задач разного типа
    // Удаление только "задач"
    public static void removeOnlyTasks() {
        HashMap<Integer, Task> epicsWithoutTasks = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("epic")) {
                epicsWithoutTasks.put(task.getId(), task);
            }
        }
        allTasks = epicsWithoutTasks;
    }

    // Удаление только "эпиков"
    public static void removeOnlyEpics() {
        HashMap<Integer, Task> tasksWithoutEpics = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("task")) {
                tasksWithoutEpics.put(task.getId(), task);
            }
        }
        allTasks = tasksWithoutEpics;
    }

    // Удаление всех подзадач конкретного эпика
    public static void removeSubTasksOfEpic(String taskName) {
        Epic taskToRemove = (Epic) getTaskById(taskName);
        if (!checkIfInputTaskExists(taskToRemove)) {
            System.out.println("Задача не найдена!");
            return;
        }
        taskToRemove.subTasks.clear();
    }

    // Удаление одной конкретной задачи конкретного эпика
    public static void removeSubTaskOfEpic(String subTaskName, String epicName) {
        Epic epic = (Epic) getTaskById(epicName);
        if (!checkIfInputTaskExists(epic)) {
            System.out.println("Задача не найдена!");
            return;
        }
        SubTask subTask = getSubTaskById(subTaskName, epicName);
        if (!checkIfInputTaskExists(subTask)) {
            System.out.println("Задача не найдена!");
            return;
        }
        epic.subTasks.remove(subTask.getSubTaskId(), subTask);
    }

    // 4. Получение разного типа задач по идентификатору
    // Получение конкретной задачи или эпика по имени, введенным пользователем
    public static Task getTaskById(String taskName) {
        Task neededTask = null;
        for (Task task : allTasks.values()) {
            if (task.hashCode() == Objects.hash(taskName)) {
                neededTask = task;
                break;
            }
        }
        return neededTask;
    }

    // Получение конкретной подзадачи по имени и по эпику, которому она принадлежит(вводятся пользователем)
    public static SubTask getSubTaskById(String taskName, String epicName) {
        Epic epic = (Epic) getTaskById(epicName);
        SubTask neededSubTask = null;
        for (SubTask subTask : epic.subTasks.values()) {
            if (subTask.name.equals(taskName)) {
                neededSubTask = subTask;
                break;
            }
        }
        return neededSubTask;
    }

    // 5. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    // Обновление задачи
    public static void updateTask(Task task, Progress progress) {
        addNewTask(task);
        task.setProgress(progress);
    }

    // Обновление эпика
    public static void updateEpic(Epic epic /*, Progress progress */) {
        addNewEpic(epic);
        // epic.setProgress(progress);
    }

    // Обновление подзадачи. При обновлении обновляется и статус эпика
    public static void updateSubTask(SubTask subTask, Progress progress) {
        addNewSubTask(subTask);
        subTask.setProgress(progress);
    }

    // 6. Обновление статуса задач
    // Обновление статуса эпика, используется только методом updateSubTaskStatus() при обновлении статуса подзадачи
    // Получается, что это что-то вроде вспомогательного метода, отдельно не используется
    public static Progress getEpicProgress(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;

        for (SubTask subTask1 : epic.subTasks.values()) {
            if (subTask1.progress != Progress.NEW) {
                isNew = false;
            } else if (subTask1.progress != Progress.DONE) {
                isDone = false;
            }
        }
        if (isNew || (epic.subTasks.size() == 0)) {
            epic.progress = Progress.NEW;
        } else if (isDone) {
            epic.progress = Progress.DONE;
        } else {
            epic.progress = Progress.IN_PROGRESS;
        }
        return epic.progress;
    }

    // Обновление статуса подзадачи. В конце вызывает метод getEpicProgress(), чтобы обновить статус всего эпика
    public static void updateSubTaskStatus(String taskName, String epicName, Progress progress) {
        SubTask subTask = getSubTaskById(taskName, epicName);
        if (!checkIfInputTaskExists(subTask)) {
            System.out.println("Задача не найдена!");
            return;
        }
        subTask.setProgress(progress);
        Epic epic = (Epic) getTaskById(epicName);
        if (!checkIfInputTaskExists(epic)) {
            System.out.println("Задача не найдена!");
            return;
        }
        getEpicProgress(epic);

    }

    // 7. Дополнительно:
        // Получение статуса задач по выбору пользователя
    public static Progress getProgressByChoice(String progressName) {
        Progress progress;
        switch (progressName) {
            case "1":
                progress = Progress.NEW;
            case "2":
                progress = Progress.IN_PROGRESS;
            case "3":
                progress = Progress.DONE;
            default:
                progress = Progress.IN_PROGRESS;
        }
        return progress;
    }

        //Проверка, что вводимая пользователем задача (при обновлении, например) действительно существует
    public static boolean checkIfInputTaskExists(Task task) {
        if (task == null) {
            return false;
        } else {
            return true;
        }
    }


}
