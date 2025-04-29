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
        System.out.println(Thread.currentThread().getName() + " attempting to request resources.");
        while (!manager.requestResources(neededResources)) {
            System.out.println(Thread.currentThread().getName() + " couldn't get all resources. Retrying...");
            try {
                Thread.sleep(100); // wait a bit and try again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(Thread.currentThread().getName() + " acquired all resources!");
        try {
            Thread.sleep(500); // simulate doing some work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        manager.releaseResources(neededResources);
        System.out.println(Thread.currentThread().getName() + " released resources.");
    }
}

