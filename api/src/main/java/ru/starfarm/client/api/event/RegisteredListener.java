package ru.starfarm.client.api.event;

import ru.starfarm.client.api.functional.Destroyable;

public interface RegisteredListener extends Destroyable {

    Class<?> getEventClass();

    void post(Object event);

}
