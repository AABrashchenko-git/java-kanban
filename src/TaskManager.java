import java.util.HashMap;

public class TaskManager {
 // методы
    // 1. хранить задачи всех типов
  public static HashMap<Integer, Task> allTasks = new HashMap<>();


    // 2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):

            //  a. Получение списка всех задач
        public static void printAllTasks() {
            int i = 1;
            for (Task task : allTasks.values()) {
                System.out.printf(i++ + ". " + task.getName() + "\n");
            }
        }
        public static void printAllSubTask() {
            String result = "";
           // for()
        }

            // b. Удаление всех задач

            // c. Получение по идентификатору

            //TODO d. Создание. Сам объект должен передаваться в качестве параметра
    public static void addNewTaskOrEpic(Task task) {
        allTasks.put(task.getId(), task);
    }
    public static void addNewSubTask(SubTask subTask) {
        for(Task task : allTasks.values()) {
            if(task.getId()==subTask.getId()) {
                Epic epic = (Epic) task;
                epic.addSubTask(subTask);
            }
        }
    }

    public static void updateTask(Task task) {
        addNewTaskOrEpic(task);
    }

    public static void updateSubTask(SubTask subTask) {
        addNewSubTask(subTask);

    }

    @Override
    public String toString() {

            for (Task task : allTasks.values()) {
                System.out.printf("Задача" + task.getName());
            }
        return "TaskManager{}";
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
                    // в Main можно создать метод, который запрашивает у пользователя все данные о задаче, и сюда тоже его вставить




            //  f. Удаление по идентификатору

    // 3.Дополнительные методы:

            // a. Получение списка всех подзадач определённого эпика






}
