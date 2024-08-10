import ru.practicum.taskTracker.http.HttpTaskServer;
import ru.practicum.taskTracker.service.Managers;

public class Main {
    public static void main(String[] args) {
        // И снова перенёс в Main, раз помещать в классе HttpTaskServer никакого сакрального смысла нет))
        HttpTaskServer server = new HttpTaskServer(Managers.getFileBackedManager());
        server.start();
    }

}