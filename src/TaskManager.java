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
        for (Epic epic : allEpics.values()) {
            if (epic.getId() == subTask.getEpicId()) {
                epic.addSubTask(subTask);
            }
        }
    }
    // 2. Получение списка каждого типа задач

    //  Получение списка всех задач
    public static void printAllTasks() {
        int i = 1;
        for (Task task : allTasks.values()) {
            System.out.println(i++ + ". " + task.getName() + ". Описание задачи: " + task.getDescription() +
                    ". Статус задачи: " + task.getProgress() + "\n");
        }
    }

    //  Получение списка всех эпиков
    public static void printAllEpics() {
        int i = 1;
        for (Epic epic : allEpics.values()) {
            System.out.println(i++ + ". " + epic.getName() + ". Описание эпика: " + epic.getDescription() +
                    ". Статус эпика: " + epic.getProgress() + "\n");
        }
    }

    //  Получение списка всех подзадач конкретного эпика
    public static void printAllSubTask(String epicName) {
        Epic epic = getEpicById(epicName);
        if (!checkIfInputTaskExists(epic)) {
            System.out.println("Эпик " + epicName + " не найден! Невозможно вывести подзадачи");
            return;
        }
        System.out.println(epic);
        /*for (Epic epic : allEpics.values()) {
            if (epic.getId() == Objects.hash(epicName)) {
                System.out.println(epic);
            }
        }*/
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
        allEpics.clear();
    }

    // Удаление всех подзадач конкретного эпика
    public static void removeSubTasksOfEpic(String epicName) {
        Epic epic = getEpicById(epicName);
        if (!checkIfInputTaskExists(epic)) {
            System.out.println("Задача не найдена!");
            return;
        }
        epic.subTasks.clear();
        epic.setProgress(getUpdatedEpicProgress(epic));
    }

    // Удаление одной конкретной задачи конкретного эпика
    public static void removeOneSubTaskOfEpic(String subTaskName, String epicName) {
        Epic epic = getEpicById(epicName);
        if (!checkIfInputTaskExists(epic)) {
            System.out.println("Эпик не найден!");
            return;
        }
        SubTask subTask = getSubTaskById(subTaskName, epicName);
        if (!checkIfInputTaskExists(subTask)) {
            System.out.println("Подзадача не найдена!");
            return;
        }
        epic.subTasks.remove(subTask.getSubTaskId(), subTask);
        epic.setProgress(getUpdatedEpicProgress(epic));
    }

    // Удаление одного эпика
    public static void removeOneEpic(String epicName) {
        Epic epic = getEpicById(epicName);
        if (!checkIfInputTaskExists(epic)) {
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
            if (task.getId() == Objects.hash(taskName)) {
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
            if (epic.getId() == Objects.hash(taskName)) {
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
    public static void updateTaskProgress(String taskName, Progress progress) {
        Task task = getTaskById(taskName);
        if (!checkIfInputTaskExists(task)) {
            System.out.println("Подзадача не найдена!");
            return;
        }
        task.setProgress(progress);
    }

    // 6. Обновление статуса задач
    // Обновление статуса эпика, используется только методом updateSubTaskStatus() при обновлении статуса подзадачи
    // Получается, что это что-то вроде вспомогательного метода, отдельно не используется
    public static Progress getUpdatedEpicProgress(Epic epic) {
        int newCount = 0;
        int doneCount = 0;
        int inProgressCount = 0;

        if (epic.subTasks.isEmpty()) {
            return Progress.NEW;
        }

        for (SubTask subTask : epic.subTasks.values()) {
            if (subTask.progress == Progress.NEW) {
                newCount++;
            } else if (subTask.progress == Progress.DONE) {
                doneCount++;
            } else {
                inProgressCount++;
            }
        }

        if (newCount > 0 && doneCount == 0 && inProgressCount == 0) {
            return Progress.NEW;
        } else if (inProgressCount == 0 && newCount == 0 && doneCount > 0) {
            return Progress.DONE;
        } else {
            return Progress.IN_PROGRESS;
        }
    }

    // Обновление статуса подзадачи. В конце вызывает метод getEpicProgress(), чтобы обновить статус всего эпика
    public static void updateSubTaskStatus(String subTaskName, String epicName, Progress progress) {
        SubTask subTask = getSubTaskById(subTaskName, epicName);
        if (!checkIfInputTaskExists(subTask)) {
            System.out.println("Подзадача не найдена!");
            return;
        }
        subTask.setProgress(progress);
        Epic epic = getEpicById(epicName);
        if (!checkIfInputTaskExists(epic)) {
            System.out.println("Эпик не найден!");
            return;
        }
        epic.progress = getUpdatedEpicProgress(epic);
    }

    // 7. Дополнительно:
    //Проверка, что вводимая пользователем задача (при обновлении, например) действительно существует
    public static boolean checkIfInputTaskExists(Task task) {
        if (task == null) {
            return false;
        } else {
            return true;
        }
    }


}
