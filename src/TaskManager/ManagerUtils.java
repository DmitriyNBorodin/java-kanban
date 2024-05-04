package TaskManager;

public class ManagerUtils {

    private ManagerUtils() {
        throw new UnsupportedOperationException();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
