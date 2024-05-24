import java.util.*;

public class TaskManager {
    public static HashMap<Integer, Task> allTasks = new HashMap<>();
    public static HashMap<Integer, Epic> allEpics = new HashMap<>();

    // 1. Создание задачи (добавление в список). Сам объект передается в качестве параметра
    // Добавление  задачи
    public static void addNewTask(Task task) {
        allTasks.put(task.getId(), task);
    }

    //  Добавление  эпика
    public static void addNewEpic(Epic epic) {
        allEpics.put(epic.getId(), epic);
    }

    //  Добавление  подзадачи (сама подзадача при создании уже содержит информацию о эпике, которому она принадлежит)
    public static void addNewSubTask(SubTask subTask) {
        for (Epic epic: allEpics.values()) {
            if (epic.getId() == subTask.getEpicId()) {
               // Epic epic = (Epic) task;
                epic.addSubTask(subTask);
            }
        }
    }

    // 2. Получение списка каждого типа задач

    //  Получение списка всех задач
    public static void printAllTasks() {
        int i = 1;
        for (Task task : allTasks.values()) {
                System.out.printf(i++ + ". " + task.getName() + "\n");
        }
    }

    //  Получение списка всех эпиков
    public static void printAllEpics() {
        int i = 1;
        for (Epic epic : allEpics.values()) {
            System.out.printf(i++ + ". " + epic.getName() + ". Статус эпика: " + epic.getProgress() + "\n");

        }
    }

    //  Получение списка всех подзадач конкретного эпика
    public static void printAllSubTask(String epicName) {
       //TODO проверить, существует ли?

        for (Epic epic : allEpics.values()) {
            if (epic.hashCode() == Objects.hash(epicName)) {
                System.out.println(epic);
            }
        }
    }

    // 3. Удаление всех задач разного типа
    // Удаление только "задач"
    public static void removeTasks() {
        /*
        HashMap<Integer, Task> epicsWithoutTasks = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("epic")) {
                epicsWithoutTasks.put(task.getId(), task);
            }
        }
        allTasks = epicsWithoutTasks;

         */
       allTasks.clear();

    }

    // Удаление только "эпиков"
    public static void removeEpics() {
/*        HashMap<Integer, Task> tasksWithoutEpics = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("task")) {
                tasksWithoutEpics.put(task.getId(), task);
            }
        }
        allTasks = tasksWithoutEpics;*/
        allEpics.clear();

    }

    // Удаление всех подзадач конкретного эпика
    public static void removeSubTasksOfEpic(String epicName) {
        Epic taskToRemove = getEpicById(epicName);
        if (!checkIfInputTaskExists(taskToRemove)) {
            System.out.println("Задача не найдена!");
            return;
        }
        taskToRemove.subTasks.clear();
    }

    // Удаление одной конкретной задачи конкретного эпика
    public static void removeOneSubTaskOfEpic(String subTaskName, String epicName) {
        Epic epic = getEpicById(epicName);
        if (!checkIfInputEpicExists(epic)) {
            System.out.println("Эпик не найден!");
            return;
        }
        SubTask subTask = getSubTaskById(subTaskName, epicName);
        if (!checkIfInputTaskExists(subTask)) {
            System.out.println("Подзадача не найдена!");
            return;
        }
        epic.subTasks.remove(subTask.getSubTaskId(), subTask);
    }
        // Удаление одного эпика
    public static void removeOneEpic(String epicName) {
        Epic epic = getEpicById(epicName);
        if (!checkIfInputEpicExists(epic)) {
            System.out.println("Эпик не найден!");
            return;
        }
        allEpics.remove(epic.getId(), epic);

    }
        // Удаление одного задания
        public static void removeOneTask(String taskName) {
            Task task = getTaskById(taskName);
            if (!checkIfInputTaskExists(task)) {
                System.out.println("Эпик не найден!");
                return;
            }
            allTasks.remove(task.getId(), task);
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
    // Получение конкретного эпика по имени, введенным пользователем
    public static Epic getEpicById(String taskName) {
        Epic neededEpic = null;
        for (Epic epic : allEpics.values()) {
            if (epic.hashCode() == Objects.hash(taskName)) {
                neededEpic = epic;
                break;
            }
        }
        return neededEpic;
    }

    // Получение конкретной подзадачи по имени и по эпику, которому она принадлежит(вводятся пользователем)
    public static SubTask getSubTaskById(String taskName, String epicName) {
        Epic epic = getEpicById(epicName);
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
    //TODO нужен ли этот метод?
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
    public static Progress updateEpicProgress(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;

        for (SubTask subTask1 : epic.subTasks.values()) {
            if (subTask1.progress != Progress.NEW) {
                isNew = false;
            } else if (subTask1.progress != Progress.DONE) {
                isDone = false;
            }
        }

        System.out.println("isnew? " + isNew);
        System.out.println("isDone? " + isDone);
        if (isNew || (epic.subTasks.size() == 0)) {
            epic.setProgress(Progress.NEW);
            System.out.println("Установили НОВЫЙ");
        } else if (isDone) {
            epic.progress = Progress.DONE;
            System.out.println("Установили СДЕЛАН");
        } else {
            epic.setProgress(Progress.IN_PROGRESS);
            System.out.println("Установили В ПРОГРЕССЕ");
        }

        return epic.progress;
    }

    // Обновление статуса подзадачи. В конце вызывает метод getEpicProgress(), чтобы обновить статус всего эпика
    public static void updateSubTaskStatus(String subTaskName, String epicName, Progress progress) {
        SubTask subTask = getSubTaskById(subTaskName, epicName);
        if (!checkIfInputTaskExists(subTask)) {
            System.out.println("Подадача не найдена!");
            return;
        }
        subTask.setProgress(progress);
        Epic epic = getEpicById(epicName);
        if (!checkIfInputTaskExists(epic)) {
            System.out.println("Эпик не найден!");
            return;
        }

        updateEpicProgress(epic);


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
    public static boolean checkIfInputEpicExists(Epic epic) {
        if (epic == null) {
            return false;
        } else {
            return true;
        }
    }


}
