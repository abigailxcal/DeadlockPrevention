import java.util.*;

class ResourceManager {
    private final List<Resource> resources;
    public ResourceManager(List<Resource> resources) {
        // sort resources by id to enforce ordering
        this.resources = new ArrayList<>(resources);
        this.resources.sort(Comparator.comparingInt(Resource::getId));
    }

    public synchronized boolean requestResources(List<Resource> requestedResources) {       // requests are made in order
        requestedResources.sort(Comparator.comparingInt(Resource::getId));
        for (Resource r : requestedResources) {
            if (!r.isAvailable()) {
                // if any resource isn't available, release anything already allocated by curr process 
                releaseResources(requestedResources);
                return false;
            }
        }

        for (Resource r : requestedResources) { //lock resources
            r.setAvailable(false);
        }
        return true;
    }

    public synchronized void releaseResources(List<Resource> resourcesToRelease) {
        for (Resource r : resourcesToRelease) {
            r.setAvailable(true);
        }
    }
}

