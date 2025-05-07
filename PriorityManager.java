import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PriorityManager {

    public static final Map<String, Integer> threadPriorities = new ConcurrentHashMap<>();

    public static void assignPriority(String threadName, int priority) {
        threadPriorities.put(threadName, priority);
    }

    public static int getPriority(String threadName) {
        return threadPriorities.getOrDefault(threadName, 0);
    }
}