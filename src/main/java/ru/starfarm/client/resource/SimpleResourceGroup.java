package ru.starfarm.client.resource;

import ru.starfarm.client.api.resource.Resource;
import ru.starfarm.client.api.resource.ResourceGroup;

import java.util.ArrayList;
import java.util.List;

public class SimpleResourceGroup implements ResourceGroup {

    private final List<Resource> resources = new ArrayList<>();

    @Override
    public void destroy() {
        resources.forEach(Resource::destroy);
        resources.clear();
    }

    @Override
    public void joinLoad() {
        resources.forEach(Resource::joinLoad);
    }

    @Override
    public boolean isLoaded() {
        return resources.stream().allMatch(Resource::isLoaded);
    }

    @Override
    public <R extends Resource> R addResource(R resource) {
        resources.add(resource);
        return resource;
    }

}
