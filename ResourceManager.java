import java.util.*;

class ResourceManager {
    private final List<Resource> resources;
    public ResourceManager(List<Resource> resources) {
        // sort resources by id to enforce ordering
        this.resources = new ArrayList<>(resources);
        this.resources.sort(Comparator.comparingInt(Resource::getId));
        System.out.println("ResourceManager initialized with resources: ");
        printResourcesCurrentState();
    }

    public synchronized boolean requestResources(List<Resource> requestedResources) {
        requestedResources.sort(Comparator.comparingInt(Resource::getId));
        List<Resource> acquired = new ArrayList<>();
        for (Resource r : requestedResources) {
            if (!r.isAvailable()) {
                System.out.println("    " + Thread.currentThread().getName() + " couldn't acquire resource " + r.getId() );
                if (!acquired.isEmpty()) {
                    System.out.println("     Releasing acquired resources [");
                    for (Resource res : acquired) {
                        System.out.print("     " + res.getId() + " ");
                    }
                    System.out.println("     ]");
                    releaseResources(acquired);
                }
                return false;
            }
            r.setAvailable(false);
            r.setThread(Thread.currentThread().getName());
            acquired.add(r);
            System.out.println("    " + Thread.currentThread().getName() + " acquired resources " + r.getId());
        }
        System.out.println("    "+Thread.currentThread().getName() + " requests COMPLETED. Now executing..."); 
        printResourcesCurrentState();
        return true;
    }
    
    // release resources because of failed requst
    public synchronized void releaseResources(List<Resource> resourcesToRelease) {
        for (Resource r : resourcesToRelease) {
            r.setAvailable(true);
            r.setThread(null);
        }  
        printResourcesCurrentState();  
    }

    // release resources after execution
    public synchronized void releaseResourcesAfterExecution(List<Resource> resourcesToRelease) {
        System.out.println("\n    " + Thread.currentThread().getName() + " finished execution. Releasing resources [" + resourcesToRelease.iterator().next().getId() + ", " + resourcesToRelease.get(1).getId()+"]");
        
        for (Resource r : resourcesToRelease) {
            r.setAvailable(true);
            r.setThread(null);
        }
        printResourcesCurrentState();    
    }

    public void printResourcesCurrentState() {
        System.out.println("\n\n---------------------------------------------------------------");
        System.out.println("                        CURRENT STATE                   ");
        System.out.println("-- Resource ID -- | ------- State ------- | ------ Thread -----");
        for (Resource r : this.resources) {
            if (r.isAvailable()) {
                System.out.println("         " + r.getId() + "        |      Available        |       ");
            } else {
                System.out.println("         " + r.getId() + "        |      Not available    |     "+r.getThread());
               
            }
        }
        System.out.println("---------------------------------------------------------------\n\n");
        
    }
}

