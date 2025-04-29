import java.util.Arrays;
import java.util.List;

public class Main {

// TODO: specify which resources are being requested by a particular process 

    
    public static void main(String[] args) {
        Resource r1 = new Resource(1);
        Resource r2 = new Resource(2);
        Resource r3 = new Resource(3);
        Resource r4 = new Resource(4);
        List<Resource> allResources = Arrays.asList(r1, r2, r3, r4);
        ResourceManager manager = new ResourceManager(allResources);

        // processes that intentionally overlap
        Process p1 = new Process(manager, Arrays.asList(r1, r3));
        Process p2 = new Process(manager, Arrays.asList(r2, r4));
        Process p3 = new Process(manager, Arrays.asList(r1, r2));
        Process p4 = new Process(manager, Arrays.asList(r3, r4));
        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        Thread t3 = new Thread(p3);
        Thread t4 = new Thread(p4);
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\nAll processes completed without deadlock.");
    }

    
}
