import java.util.*;

public class TaskManager {
    static int taskCounter;
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
        //TODO ХММММ а КАК НАСЧЕТ СЮДА ДОБАВИТЬ ИНИЦИАЛИЗАЦИЮ ЭПИК АЙДИ?
       /// subTask.setEpicId(epicId);
        epic.addSubTask(subTask);
    }
// e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    // 1) Задачи
    public static Task updateTask(Task task) {
        //TODO проверку на null надо
        // ХММММ а КАК НАСЧЕТ task.setStatus(>>e0i0rsjeoi
        Task updatedTask =  allTasks.replace(task.getId(), task);
        return updatedTask;
    }
    // 2) Эпики
    public static Epic updateEpic(Epic epic) {
        //TODO проверку на null надо
        Epic updatedEpic =  allEpics.replace(epic.getId(), epic);
        return updatedEpic;
    }
    // 3) Подзадачи
    public static SubTask updateSubTask(SubTask subTask) {
        //TODO проверку на null надо
        System.out.println("!!!!!!!!!!!" + subTask.getEpicId());
        Epic epicOfSubTask = allEpics.get(subTask.getEpicId());

        System.out.println("!!!!!!!!!!!" + allEpics.get(subTask.getEpicId()));
        SubTask updatedSubTask = epicOfSubTask.subTasks.replace(subTask.getId(), subTask);
        epicOfSubTask.setStatus(getUpdatedEpicStatus(epicOfSubTask));
        return updatedSubTask;
    }

    // Обновление статуса эпика, используется только методом updateSubTaskStatus() при обновлении статуса подзадачи
    // Получается, что это что-то вроде вспомогательного метода, отдельно не используется
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


// c. Получение по идентификатору.
    // 1) Задачи

    // 2) Эпики

    // 3) Подзадачи

// a. Получение списка всех задач.
    // 1) Задачи

    // 2) Эпики

    // 3) Подзадачи

// b. Удаление всех задач.
    // 1) Задачи

    // 2) Эпики

    // 3) Подзадачи


// f. Удаление по идентификатору.
    // 1) Задачи

    // 2) Эпики

    // 3) Подзадачи
//Дополнительные методы:

//a. Получение списка всех подзадач определённого эпика.


public static int getNextId() {
    int nextId = ++taskCounter;
    return nextId;
}

    public static HashMap<Integer, Task> getAllTasks() {
        return allTasks;
    }

    public static HashMap<Integer, Epic> getAllEpics() {
        return allEpics;
    }






























/*

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
                    ". Статус задачи: " + task.getStatus() + "\n");
        }
    }
    //  Получение списка всех эпиков
    public static void printAllEpics() {
        int i = 1;
        for (Epic epic : allEpics.values()) {
            System.out.println(i++ + ". " + epic.getName() + ". Описание эпика: " + epic.getDescription() +
                    ". Статус эпика: " + epic.getStatus() + "\n");
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
    }
    // 3. Удаление всех задач разного типа
    // Удаление только "задач"
    public static void removeTasks() {
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
        epic.setStatus(getUpdatedEpicStatus(epic));
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
        epic.setStatus(getUpdatedEpicStatus(epic));
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
            if (subTask.getName().equals(taskName)) {
                neededSubTask = subTask;
                break;
            }
        }
        return neededSubTask;
    }
    // 5. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    // Обновление задачи
    public static void updateTaskStatus(String taskName, Status status) {
        Task task = getTaskById(taskName);
        if (!checkIfInputTaskExists(task)) {
            System.out.println("Подзадача не найдена!");
            return;
        }
        task.setStatus(status);
    }
    // 6. Обновление статуса задач
    // Обновление статуса эпика, используется только методом updateSubTaskStatus() при обновлении статуса подзадачи
    // Получается, что это что-то вроде вспомогательного метода, отдельно не используется
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

    // Обновление статуса подзадачи. В конце вызывает метод getEpicStatus(), чтобы обновить статус всего эпика
    public static void updateSubTaskStatus(String subTaskName, String epicName, Status status) {
        SubTask subTask = getSubTaskById(subTaskName, epicName);
        if (!checkIfInputTaskExists(subTask)) {
            System.out.println("Подзадача не найдена!");
            return;
        }
        subTask.setStatus(status);
        Epic epic = getEpicById(epicName);
        if (!checkIfInputTaskExists(epic)) {
            System.out.println("Эпик не найден!");
            return;
        }
        epic.setStatus(getUpdatedEpicStatus(epic));
    }

    // 7. Дополнительно:
    //Проверка, что вводимая пользователем задача (при обновлении, например) действительно существует
    public static boolean checkIfInputTaskExists(Task task) {
        if (task == null) {
            return false;
        } else {
            return true;
        }
    }*/
}
