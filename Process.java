import java.util.List;

class Process implements Runnable {
    private final BaseResourceManager manager;
    private final List<Resource> neededResources;
    private int retryCount = 0;

    public Process(BaseResourceManager manager, List<Resource> neededResources) {
        this.manager = manager;
        this.neededResources = neededResources;

    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        long startTime = System.currentTimeMillis();
        if (this.manager.verbose) {
            System.out.println(threadName+" attempting to acquire resources " + neededResources.stream().map(r -> "" + r.getId()).toList() );
        }
        
        while (!manager.requestResources(neededResources)) {
            retryCount++;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // System.out.println(threadName + " acquired resources " + neededResources.stream().map(r -> "" + r.getId()).toList());

        try {
            Thread.sleep(500); // simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        manager.releaseResourcesAfterExecution(neededResources);
        //manager.releaseResources(neededResources);
        long elapsed = System.currentTimeMillis() - startTime;
        // System.out.println(threadName + " released resources " + neededResources.stream().map(r -> "" + r.getId()).toList() +
        //        " | Retries: " + retryCount + " | Time: " + elapsed + "ms");
    }
}
