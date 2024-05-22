public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        String taskType = "Эпик";
        String name = "поесть";
        String description = "поесть вкусное что-нибудь";
        String progress = String.valueOf(Progress.IN_PROGRESS);
        Progress status = Progress.IN_PROGRESS;
        String epicName = "подзача1";



        Task task;

        switch(taskType) {
            case "Подзадача":
                task= new SubTask(epicName, name, description, status);
                TaskManager.addNewSubTask((SubTask) task);
                break;
            case "Задача":
                task = new Task(name, description, status);
                TaskManager.addNewTaskOrEpic(task);
                break;
            case "Эпик":
            default:
                task = new Epic(name, description, status);
                TaskManager.addNewTaskOrEpic(task);
                break;
        }

        Task task1 = new Task("задача1", "поесть",  Progress.DONE);
        Task task2 = new Epic("эпик1", "поработать", Progress.DONE);
        Task task3 = new SubTask("эпик1", "Подзадача1", "Поработать", Progress.DONE);

        TaskManager.addNewTaskOrEpic(task1);
        TaskManager.addNewTaskOrEpic(task2);
        TaskManager.addNewSubTask((SubTask) task3);

        Task task4 = new SubTask("эпик1", "Подзадача2", "Поработать2", Progress.DONE);
        TaskManager.addNewSubTask((SubTask) task4);

        System.out.println(TaskManager.allTasks);
        System.out.println(((Epic) task2).subTasks);

        TaskManager.printAllTasks();



    }





}
