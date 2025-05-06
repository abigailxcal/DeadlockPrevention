import java.util.List;

abstract class BaseResourceManager {
    protected final List<Resource> resources;
    protected final boolean verbose;

    public BaseResourceManager(List<Resource> resources, boolean verbose) {
        this.verbose = verbose;
        this.resources = resources;
        this.resources.sort((r1, r2) -> Integer.compare(r1.getId(), r2.getId()));
    }

    public abstract boolean requestResources(List<Resource> requestedResources);

    public synchronized void releaseResources(List<Resource> resourcesToRelease) {
        for (Resource r : resourcesToRelease) {
            r.setAvailable(true);
            r.setThread(null);
        }
        //printResourcesCurrentState();  
    }

    // release resources after execution
    public synchronized void releaseResourcesAfterExecution(List<Resource> resourcesToRelease) {
        if (verbose) {
            System.out.println("\n    " + Thread.currentThread().getName() + " finished execution. Releasing resources [" 
                + resourcesToRelease.get(0).getId() + ", " + resourcesToRelease.get(1).getId() + "]");
        }
        
        //System.out.println("\n    " + Thread.currentThread().getName() + " finished execution. Releasing resources [" + resourcesToRelease.iterator().next().getId() + ", " + resourcesToRelease.get(1).getId()+"]");
        
        for (Resource r : resourcesToRelease) {
            r.setAvailable(true);
            r.setThread(null);
        }
        printResourcesCurrentState();    
    }
    
    public Resource getResourceById(int id) {
        for (Resource r : resources) {
            if (r.getId() == id) return r;
        }
        throw new IllegalArgumentException("Resource with ID " + id + " not found.");
    }

    public void printResourcesCurrentState() {
        if (!verbose) return;
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
