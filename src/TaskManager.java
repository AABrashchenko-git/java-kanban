import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    // методы
    // 1. хранить задачи всех типов
    public static HashMap<Integer, Task> allTasks = new HashMap<>();


    // 2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):

    //  a. Получение списка всех задач
    public static void printAllTasks() {
        int i = 1;
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("task")) {
                System.out.printf(i++ + ". " + task.getName() + "\n");
            }
        }
    }
    public static void printAllEpics() {
        int i = 1;
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("epic")) {
                System.out.printf(i++ + ". " + task.getName() + "\n");
            }
        }
    }
    public static void printAllSubTask(String epicName) {
        for (Task task : allTasks.values()) {
            if (task.hashCode()== Objects.hash(epicName)) {
                System.out.println(task);
            }
        }


    }

    // b. Удаление всех задач

    // c. Получение по идентификатору
    public static Task getTaskById(String taskName) {
        Task neededTask = null;
        for (Task task : allTasks.values()) {
            if (task.hashCode()== Objects.hash(taskName)) {
                neededTask = task;
                break;
            }
        }
        if (neededTask == null) {
        System.out.println("Задача не найдена!");
        }
    return neededTask;
    }

    //TODO d. Создание. Сам объект должен передаваться в качестве параметра
    public static void addNewTask(Task task) {
        allTasks.put(task.getId(), task);
    }

    public static void addNewEpic(Epic epic) {
        allTasks.put(epic.getId(), epic);
    }

    public static void addNewSubTask(SubTask subTask) {
        for (Task task : allTasks.values()) {
            if (task.getId() == subTask.getId()) {
                Epic epic = (Epic) task;
                epic.addSubTask(subTask);
            }
        }
    }

    public static void updateTask(String taskName) {
       // getTaskById();
    }

    public static void updateSubTask(SubTask subTask) {
        addNewSubTask(subTask);

    }
        //TODO метод нерабочий, исправить, реализовать метод получения подзадачи из эпика и имени подзадачи
    public static void updateSubTaskStatus(Progress progress, String subTaskName, String epicName) {
            Epic epic = (Epic) getTaskById(epicName);
            SubTask subTaskCheck = (SubTask) getTaskById(subTaskName);
            for(Integer key : epic.subTasks.keySet()) {
               SubTask subTask = epic.subTasks.get(key);
               if(subTask.equals(subTaskCheck )) {
                   subTask.setProgress(progress);
               }

        }
    }


    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра

    // в Main можно создать метод, который запрашивает у пользователя все данные о задаче, и сюда тоже его вставить


    //  f. Удаление по идентификатору

    // 3.Дополнительные методы:

    // a. Получение списка всех подзадач определённого эпика


}
