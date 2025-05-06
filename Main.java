import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("========== DEADLOCK PREVENTION STRATEGIES ==========");
            System.out.println("1. Run Circular Wait Prevention");
            System.out.println("2. Run Hold and Wait Prevention");
            System.out.println("3. Run Preemption Strategy");
            System.out.println("4. Run test cycle for ALL strategies (10 runs each)");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    runSingleStrategy("Circular Wait", new CircularWaitResourceManager(createResources(),true));
                    break;
                case "2":
                    runSingleStrategy("Hold and Wait", new HoldAndWaitResourceManager(createResources(),true));
                    break;
                case "3":
                    runSingleStrategy("Preemption", new PreemptiveResourceManager(createResources(),true));
                    break;
                case "4":
                    runAllStrategies(10); 
                    break;
                case "5":
                    System.out.println("Exiting. Goodbye.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please enter 1-5.\n");
            }
        }

        scanner.close();
    }

    private static void runSingleStrategy(String name, BaseResourceManager manager) {
        runTestWithManager(manager, name, true);
    }

    private static void runAllStrategies(int testRuns) {
        long circularTotal = 0;
        long holdTotal = 0;
        long preemptTotal = 0;

        for (int i = 1; i <= testRuns; i++) {
            System.out.println("\n===== TEST RUN " + i + " =====");

            circularTotal += runTestWithManager(new CircularWaitResourceManager(createResources(),false), "Circular Wait",false);
            holdTotal += runTestWithManager(new HoldAndWaitResourceManager(createResources(),false), "Hold and Wait",false);
            preemptTotal += runTestWithManager(new PreemptiveResourceManager(createResources(),false), "Preemption",false);
        }

        System.out.println("===== AVERAGE TIMES OVER " + testRuns + " RUNS =====");
        System.out.println("Circular Wait Average Time: " + (circularTotal / testRuns) + "ms");
        System.out.println("Hold and Wait Average Time: " + (holdTotal / testRuns) + "ms");
        System.out.println("Preemption Average Time: " + (preemptTotal / testRuns) + "ms\n");
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

    private static long runTestWithManager(BaseResourceManager manager, String strategyName, boolean verbose) {
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
