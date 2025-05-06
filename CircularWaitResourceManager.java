import java.util.*;

public class CircularWaitResourceManager extends BaseResourceManager {
    public CircularWaitResourceManager(List<Resource> resources) {
        super(resources);
    }

    @Override
    public synchronized boolean requestResources(List<Resource> requestedResources) {
        requestedResources.sort(Comparator.comparingInt(Resource::getId));
        List<Resource> acquired = new ArrayList<>();

        for (Resource r : requestedResources) {
            if (!r.isAvailable()) {
                releaseResources(acquired);
                return false;
            }
            r.setAvailable(false);
            r.setThread(Thread.currentThread().getName());
            acquired.add(r);
            //System.out.println("Resource " + r.getId() + " acquired by " + r.getThread());
        }

        return true;
    }
}
