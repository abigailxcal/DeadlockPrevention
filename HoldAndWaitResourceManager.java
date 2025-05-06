import java.util.*;

public class HoldAndWaitResourceManager extends BaseResourceManager {
    public HoldAndWaitResourceManager(List<Resource> resources) {
        super(resources);
    }

    @Override
    public synchronized boolean requestResources(List<Resource> requestedResources) {
        requestedResources.sort(Comparator.comparingInt(Resource::getId));

        for (Resource r : requestedResources) {
            if (!r.isAvailable()) {
                return false;
            }
        }

        for (Resource r : requestedResources) {
            r.setAvailable(false);
            r.setThread(Thread.currentThread().getName());
            //System.out.println("Resource " + r.getId() + " acquired by " + r.getThread());
        }

        return true;
    }
}
