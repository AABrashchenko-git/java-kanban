import java.util.HashMap;

public class TaskManager {
  // public String taskType;
  public static int id;

 // методы
    // 1. хранить задачи всех типов
  public  static HashMap<Integer, Task> allTasks = new HashMap<>();


    // 2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):

            //  a. Получение списка всех задач

            // b. Удаление всех задач

            // c. Получение по идентификатору

            //TODO d. Создание. Сам объект должен передаваться в качестве параметра
    public static Task composeNewTask(String taskType, String name, String description, Progress status) {
        Task task;
        switch(taskType) {
            case "Эпик":
                task = new Epic(name, description, status);
                break;
            case "Подзадача":
                task = new SubTask(name, description, status);
                break;
            case "Задача":
            default:
                task = new Task(name, description, status);
                break;
        }
        return task;
    }

    public static void addNewTask(Task task) {
        // int id = Task.getId() + 1;
        allTasks.put(id, task);
        id++;
    }






            // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра

            //  f. Удаление по идентификатору

    // 3.Дополнительные методы:

            // a. Получение списка всех подзадач определённого эпика






}
