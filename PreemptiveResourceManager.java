import java.util.*;

class PreemptiveResourceManager extends BaseResourceManager {

    public PreemptiveResourceManager(List<Resource> resources, boolean verbose) {
        super(resources, verbose);
    }

    @Override
    public synchronized boolean requestResources(List<Resource> requestedResources) {
        requestedResources.sort(Comparator.comparingInt(Resource::getId));
        List<Resource> acquired = new ArrayList<>();
    
        for (Resource r : requestedResources) {
            if (!r.isAvailable()) {
                String holder = r.getThread();
                int holderPriority = PriorityManager.getPriority(holder);
                int requesterPriority = PriorityManager.getPriority(Thread.currentThread().getName());
    
                if (requesterPriority > holderPriority) {
                    r.setAvailable(true);
                    r.setThread(null);
    
                    r.setAvailable(false);
                    r.setThread(Thread.currentThread().getName());
                    acquired.add(r);
    
                    if (verbose) {
                        System.out.println("    " + Thread.currentThread().getName() +
                                " preempted resource " + r.getId() + " from " + holder);
                    }
                } else {
                    if (verbose) {
                        System.out.print("    " + Thread.currentThread().getName() + 
                                " couldn't acquire resource " + r.getId() + " (held by equal/higher priority " + holder + "). ");
                        if (!acquired.isEmpty()) {
                            System.out.print("Releasing acquired resources [ ");
                            for (Resource res : acquired) {
                                System.out.print(res.getId() + " ");
                            }
                            System.out.print("]  ");
                        }
                        System.out.println("Retrying...");
                    }
                    releaseResources(acquired);
                    return false;
                }
            } else {
                r.setAvailable(false);
                r.setThread(Thread.currentThread().getName());
                acquired.add(r);
    
                if (verbose) {
                    System.out.println("    " + Thread.currentThread().getName() +
                            " acquired resource " + r.getId());
                }
            }
        }
        if (verbose) {
            System.out.println("    " + Thread.currentThread().getName() +
                    " requests COMPLETED. Now executing task...");
        }
        printResourcesCurrentState();
        return true;
    }
    
}
