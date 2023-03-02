package ru.starfarm.client.api.resource;

import ru.starfarm.client.api.functional.Destroyable;

public interface ResourceGroup extends Destroyable {

    void joinLoad();

    boolean isLoaded();

    <R extends Resource> R addResource(R resource);

}
