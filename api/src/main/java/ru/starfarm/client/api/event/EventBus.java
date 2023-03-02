package ru.starfarm.client.api.event;

import ru.starfarm.client.api.functional.Destroyable;

import java.util.function.Consumer;

public interface EventBus extends Destroyable {

    <E> E fire(E event);

    boolean fireGetResult(Object event);

    RegisteredListener register(Object... listenerObjects);

    RegisteredListener register(Class<?>... listenerClasses);

    <E> RegisteredListener register(Class<E> eventClass, Consumer<E> handler);

    void unregister(RegisteredListener listener);

}
