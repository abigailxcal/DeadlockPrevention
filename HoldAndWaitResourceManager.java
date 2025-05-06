import java.util.*;

public class HoldAndWaitResourceManager extends BaseResourceManager {
    public HoldAndWaitResourceManager(List<Resource> resources, boolean verbose) {
        super(resources, verbose);
    }

    @Override
    public synchronized boolean requestResources(List<Resource> requestedResources) {
        requestedResources.sort(Comparator.comparingInt(Resource::getId));

        for (Resource r : requestedResources) {
            if (!r.isAvailable()) {

                if (verbose) {
                    System.out.println(
                            "    " + Thread.currentThread().getName() + " couldn't acquire resource " + r.getId());
                }
                return false;
            }
        }
        for (Resource r : requestedResources) {
            r.setAvailable(false);
            r.setThread(Thread.currentThread().getName());

            if (verbose) {
                System.out.println("    " + Thread.currentThread().getName() + " acquired resource " + r.getId());
            }
        }

        if (verbose) {
            System.out
                    .println("    " + Thread.currentThread().getName() + " requests COMPLETED. Now executing task...");
        }
        printResourcesCurrentState();
        return true;
    }
}
