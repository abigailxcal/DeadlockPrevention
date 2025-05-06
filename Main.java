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
            System.out.println("4. Run test cycle for ALL strategies");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    runSingleStrategy("Circular Wait", new CircularWaitResourceManager(createResources(), true), generateRandomResourcePairs(6, 6));
                    break;
                case "2":
                    runSingleStrategy("Hold and Wait", new HoldAndWaitResourceManager(createResources(), true), generateRandomResourcePairs(6, 6));
                    break;
                case "3":
                    runSingleStrategy("Preemption", new PreemptiveResourceManager(createResources(), true), generateRandomResourcePairs(6, 6));
                    break;
                case "4":
                    int runs = 10;
                    int resources = 6;
                    int processes = 6;
                
                    try {
                        System.out.print("How many test runs would you like to do? ");
                        runs = Integer.parseInt(scanner.nextLine().trim());
                
                        System.out.print("How many resources should each strategy have? (max 6)");
                        resources = Integer.parseInt(scanner.nextLine().trim());
                
                        System.out.print("How many processes (threads) should be created? ");
                        processes = Integer.parseInt(scanner.nextLine().trim());

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input detected. Defaulting to 10 test runs, 6 resources, and 6 processes.");
                        runs = 10;
                        resources = 6;
                        processes = 6;
                    }
                
                    runAllStrategies(runs, processes, resources);
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

    private static void runSingleStrategy(String name, BaseResourceManager manager, List<List<Integer>> resourcePairs) {
        System.out.println("Assigned resource pairs:");
        for (int j = 0; j < resourcePairs.size(); j++) {
            List<Integer> pair = resourcePairs.get(j);
            System.out.printf("  P%d -> [%d, %d]%n", j + 1, pair.get(0), pair.get(1));
        }
        System.out.println();
        runTestWithManager(manager, name, resourcePairs);
    }

    private static void runAllStrategies(int testRuns, int numThreads, int numResources) {
        long circularTotal = 0;
        long holdTotal = 0;
        long preemptTotal = 0;

        for (int i = 1; i <= testRuns; i++) {
            System.out.println("===== TEST RUN " + i + " =====");
            List<List<Integer>> resourcePairs = generateRandomResourcePairs(numThreads, numResources);

            System.out.println("Assigned resource pairs:");
            for (int j = 0; j < resourcePairs.size(); j++) {
                List<Integer> pair = resourcePairs.get(j);
                System.out.printf("  P%d -> [%d, %d]%n", j + 1, pair.get(0), pair.get(1));
            }
            System.out.println();

            circularTotal += runTestWithManager(new CircularWaitResourceManager(createResources(), false), "Circular Wait", resourcePairs);
            holdTotal += runTestWithManager(new HoldAndWaitResourceManager(createResources(), false), "Hold and Wait", resourcePairs);
            preemptTotal += runTestWithManager(new PreemptiveResourceManager(createResources(), false), "Preemption", resourcePairs);
        }

        System.out.println("===== AVERAGE COMPLETION TIMES =====");
        System.out.println("Test Runs   : " + testRuns);
        System.out.println("Threads     : " + numThreads);
        System.out.println("Resources   : " + numResources);
        System.out.println("------------------------------------");
        System.out.println("Circular Wait : " + (circularTotal / testRuns) + " ms");
        System.out.println("Hold and Wait : " + (holdTotal / testRuns) + " ms");
        System.out.println("Preemption    : " + (preemptTotal / testRuns) + " ms\n");
    }

    private static List<Resource> createResources() {
        return Arrays.asList(
            new Resource(1), new Resource(2), new Resource(3),
            new Resource(4), new Resource(5), new Resource(6));
    }

    private static List<List<Integer>> generateRandomResourcePairs(int numThreads, int numResources) {
        List<List<Integer>> pairs = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            List<Integer> ids = new ArrayList<>();
            for (int j = 1; j <= numResources; j++) ids.add(j);
            Collections.shuffle(ids);
            pairs.add(List.of(ids.get(0), ids.get(1)));
        }
        return pairs;
    }

    private static long runTestWithManager(BaseResourceManager manager, String strategyName, List<List<Integer>> resourcePairs) {
        System.out.println("=== Strategy: " + strategyName + " ===");

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < resourcePairs.size(); i++) {
            List<Integer> pair = resourcePairs.get(i);
            Resource r1 = manager.getResourceById(pair.get(0));
            Resource r2 = manager.getResourceById(pair.get(1));

            threads.add(new Thread(new Process(manager, new ArrayList<>(List.of(r1, r2))), "P" + (i + 1)));
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