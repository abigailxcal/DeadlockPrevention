import java.util.*;

class ResourceManager {
    private final List<Resource> resources;
    public ResourceManager(List<Resource> resources) {
        // sort resources by id to enforce ordering
        this.resources = new ArrayList<>(resources);
        this.resources.sort(Comparator.comparingInt(Resource::getId));
    }

    public synchronized boolean requestResources(List<Resource> requestedResources) {
        requestedResources.sort(Comparator.comparingInt(Resource::getId));
        List<Resource> acquired = new ArrayList<>();
    
        for (Resource r : requestedResources) {
            if (!r.isAvailable()) {
                // release only already-acquired resources
                releaseResources(acquired);
                return false;
            }
            r.setAvailable(false);
            r.setThread(Thread.currentThread().getName());
            System.out.println("Resource " + r.getId() + " acquired by " + r.getThread());
            acquired.add(r);
        }
    
        return true;
    }
    

    public synchronized void releaseResources(List<Resource> resourcesToRelease) {
        for (Resource r : resourcesToRelease) {
            System.out.println("Resource  " + r.getId() + " has been released by " + r.getThread());
            r.setAvailable(true);
            r.setThread(null);
        }
    }
}

