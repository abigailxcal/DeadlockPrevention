import java.util.List;

abstract class BaseResourceManager {
    protected final List<Resource> resources;

    public BaseResourceManager(List<Resource> resources) {
        this.resources = resources;
        this.resources.sort((r1, r2) -> Integer.compare(r1.getId(), r2.getId()));
    }

    public abstract boolean requestResources(List<Resource> requestedResources);

    public synchronized void releaseResources(List<Resource> resourcesToRelease) {
        for (Resource r : resourcesToRelease) {
            r.setAvailable(true);
            r.setThread(null);
        }
    }
    
    public Resource getResourceById(int id) {
        for (Resource r : resources) {
            if (r.getId() == id) return r;
        }
        throw new IllegalArgumentException("Resource with ID " + id + " not found.");
    }
    
}
