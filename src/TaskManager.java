import java.util.*;

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
            if (task.hashCode() == Objects.hash(epicName)) {
                System.out.println(task);
            }
        }


    }

    // b. Удаление всех задач
    public static void removeOnlyEpics() {
        HashMap<Integer, Task> tasksWithoutEpics = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("task")) {
                tasksWithoutEpics.put(task.getId(), task);
            }
        }
        allTasks=tasksWithoutEpics;
    }

    public static void removeOnlyTasks() {
        HashMap<Integer, Task> epicsWithoutTasks = new HashMap<>();
        for (Task task : allTasks.values()) {
            if (task.taskType.equals("epic")) {
                epicsWithoutTasks.put(task.getId(), task);
            }
        }
        allTasks=epicsWithoutTasks;
    }

    public static void removeSubTasksOfEpic(String taskName) {
        Epic taskToRemove = (Epic) getTaskById(taskName);
        taskToRemove.subTasks.clear();


    }



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

    public static SubTask getSubTaskById(String taskName, String epicName) {
       Epic epic = (Epic) getTaskById(epicName);


        SubTask neededSubTask = null;
        for (SubTask subTask : epic.subTasks.values()) {
            if (subTask.hashCode() == Objects.hash(taskName)) {
                neededSubTask = subTask;
                break;
            }
        }
        if (neededSubTask == null) {
            System.out.println("Задача не найдена!");
        }
        return neededSubTask;
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


        //TODO метод нерабочий, исправить, реализовать метод получения подзадачи из эпика и имени подзадачи
/*    public static void updateSubTaskStatus(Progress progress, String subTaskName, String epicName) {
        SubTask subTaskCheck = getSubTaskById(subTaskName, epicName);
        subTaskCheck.setProgress(progress);
    }*/

    public static Progress getProgressByChoice(String progressName) {
        Progress progress;
        switch (progressName) {
            case "1": progress = Progress.NEW;
            case "2": progress = Progress.IN_PROGRESS;
            case "3": progress = Progress.DONE;
            default:  progress = Progress.IN_PROGRESS;
        }
        return progress;
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    public static void updateEpic(Epic epic, Progress progress) {
        addNewEpic(epic);
        epic.setProgress(progress);
    }

    public static void updateTask(Task task, Progress progress) {
        addNewTask(task);
        task.setProgress(progress);
    }

    public static void updateSubTask(SubTask subTask, Progress progress) {
        addNewSubTask(subTask);
        subTask.setProgress(progress);

    }

    public static Progress getEpicProgress(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;

        for(SubTask subTask1 : epic.subTasks.values()) {
            if(subTask1.progress != Progress.NEW) {
                isNew=false;
            } else if (subTask1.progress != Progress.DONE)  {
                isDone=false;
            }
        }

        if(isNew || (epic.subTasks.size() == 0)) {
            epic.progress = Progress.NEW;
        } else if(isDone) {
            epic.progress = Progress.DONE;
        } else {
            epic.progress = Progress.IN_PROGRESS;
        }
        return epic.progress;
    }

    public static void updateSubTaskStatus(String taskName, String epicName, Progress progress) {
        SubTask subTask = getSubTaskById(taskName, epicName);
        subTask.setProgress(progress);
        Epic epic = (Epic) getTaskById(epicName);
        getEpicProgress(epic);

    }

    // в Main можно создать метод, который запрашивает у пользователя все данные о задаче, и сюда тоже его вставить


    //  f. Удаление по идентификатору
    public static void removeSubTaskOfEpic(String subTaskName, String epicName) {
       Epic epic = (Epic) getTaskById(epicName);
        SubTask subTask = getSubTaskById(subTaskName, epicName);
        System.out.println(subTask.getId());
        System.out.println("!!!!!!!!!!!!!!!!");
        System.out.println(epic.subTasks);
        System.out.println(epic.subTaskId);
    epic.subTasks.remove(epic.subTaskId);

    }





    // 3.Дополнительные методы:

    // a. Получение списка всех подзадач определённого эпика


}
