import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int testRuns = 10;
        long circularTotal = 0;
        long holdTotal = 0;
        long preemptTotal = 0;

        for (int i = 1; i <= testRuns; i++) {
            System.out.println("===== TEST RUN " + i + " =====");

            List<Resource> resources1 = createResources();
            circularTotal += runTestWithManager(new CircularWaitResourceManager(resources1), "Circular Wait");
            
            List<Resource> resources2 = createResources();
            holdTotal += runTestWithManager(new HoldAndWaitResourceManager(resources2), "Hold and Wait");
            
            List<Resource> resources3 = createResources();
            preemptTotal += runTestWithManager(new PreemptiveResourceManager(resources3), "Preemption");
        }

        System.out.println("===== AVERAGE TIMES OVER " + testRuns + " RUNS =====");
        System.out.println("Circular Wait Average Time: " + (circularTotal / testRuns) + "ms");
        System.out.println("Hold and Wait Average Time: " + (holdTotal / testRuns) + "ms");
        System.out.println("Preemption Average Time: " + (preemptTotal / testRuns) + "ms");
            }

    private static List<Resource> createResources() {
        return Arrays.asList(new Resource(1), new Resource(2), new Resource(3), new Resource(4));
    }

    private static List<Resource> getRandomResourcePair(BaseResourceManager manager) {
        List<Resource> all = new ArrayList<>(List.of(
            manager.getResourceById(1),
            manager.getResourceById(2),
            manager.getResourceById(3),
            manager.getResourceById(4)
        ));
        Collections.shuffle(all);
        return all.subList(0, 2);
    }

    private static long runTestWithManager(BaseResourceManager manager, String strategyName) {
        System.out.println("=== Strategy: " + strategyName + " ===");
    
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            List<Resource> randomPair = getRandomResourcePair(manager);
            threads.add(new Thread(new Process(manager, new ArrayList<>(randomPair)), "P" + i));
        }
    
        long start = System.currentTimeMillis();
    
        for (Thread t : threads) t.start();
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    
        long totalTime = System.currentTimeMillis() - start;
        System.out.println("All processes completed. Total time: " + totalTime + "ms\n");
        return totalTime;
    }
    
}
