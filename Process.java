import java.util.List;


class Process implements Runnable {
    private final ResourceManager manager;
    private final List<Resource> neededResources;

    public Process(ResourceManager manager, List<Resource> neededResources) {
        this.manager = manager;
        this.neededResources = neededResources;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " attempting to request resources: " + neededResources.iterator().next().getId() + " " + neededResources.get(1).getId());
        while (!manager.requestResources(neededResources)) {
            try {
                Thread.sleep(100); // wait a bit and try again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            Thread.sleep(500); // simulate doing some work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        manager.releaseResourcesAfterExecution(neededResources);
        
    }

    public void printResourceRequests() {
        System.out.print(Thread.currentThread().getName() + " (p) requested resources: ");
        for (Resource r : neededResources) {
            System.out.print(r.getId() + " ");
        }
        System.out.println();
    }
}

