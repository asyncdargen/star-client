package ru.starfarm.client.api.resource;

import ru.starfarm.client.api.functional.Destroyable;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public interface Resource extends Destroyable {

    InputStream openInputStream();

    void joinLoad();

    void joinLoad(long time, TimeUnit timeUnit);

    boolean isLoaded();

}
