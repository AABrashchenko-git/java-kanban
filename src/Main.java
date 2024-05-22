public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        String typeChoice = "Эпик";
        String name = "поесть";
        String description = "поесть вкусное что-нибудь";
        String progress = String.valueOf(Progress.IN_PROGRESS);
        Progress status = Progress.IN_PROGRESS;
        String epicName = "подзача1";



        Task task;

        switch(typeChoice) {
            case "Подзадача":
                task = new SubTask(epicName, name, description, status);
                TaskManager.addNewSubTask((SubTask) task);
                break;
            case "Задача":
                task = new Task(name, description, status);
                TaskManager.addNewTask(task);
                break;
            case "Эпик":
                task = new Epic(name, description, status);
                TaskManager.addNewEpic((Epic) task);
                break;
            default:
                break;
        }

        Task task1 = new Task("задача1", "поесть",  Progress.DONE);
        Epic task2 = new Epic("эпик1", "поработать", Progress.DONE);
        Task task3 = new SubTask("эпик1", "Подзадача1", "Поработать", Progress.DONE);

        TaskManager.addNewTask(task1);
        TaskManager.addNewEpic(task2);
        TaskManager.addNewSubTask((SubTask) task3);

        Task task4 = new SubTask("эпик1", "Подзадача2", "Поработать2", Progress.DONE);
        TaskManager.addNewSubTask((SubTask) task4);

        System.out.println(TaskManager.allTasks);
        System.out.println(task2.subTasks);

        TaskManager.printAllTasks();
        System.out.println(task2.getClass());

        System.out.println(task2.toString());
        System.out.println();
        TaskManager.printAllSubTask("эпик1");
        System.out.println();
        TaskManager.printAllEpics();
        System.out.println();
        TaskManager.printAllTasks();
        System.out.println();
        System.out.println(TaskManager.getTaskById("задача1"));

        TaskManager.printAllSubTask("эпик1");

       TaskManager.updateSubTaskStatus(Progress.NEW, "Подзадача1", "эпик1");

        TaskManager.printAllSubTask("эпик1");



    }





}
