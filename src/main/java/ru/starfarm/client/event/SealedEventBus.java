package ru.starfarm.client.event;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.starfarm.client.api.event.EventBus;
import ru.starfarm.client.api.event.RegisteredListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class SealedEventBus implements EventBus {

    protected final EventBus globalEventBus;
    protected final List<RegisteredListener> listeners = new ArrayList<>();

    @Override
    public <E> E fire(E event) {
        return globalEventBus.fire(event);
    }

    @Override
    public boolean fireGetResult(Object event) {
        return globalEventBus.fireGetResult(event);
    }

    @Override
    public RegisteredListener register(Object... listenerObjects) {
        val listener = globalEventBus.register(listenerObjects);
        listeners.add(listener);
        return listener;
    }

    @Override
    public RegisteredListener register(Class<?>... listenerClasses) {
        val listener = globalEventBus.register(listenerClasses);
        listeners.add(listener);
        return listener;
    }

    @Override
    public <E> RegisteredListener register(Class<E> eventClass, Consumer<E> handler) {
        val listener = globalEventBus.register(eventClass, handler);
        listeners.add(listener);
        return listener;
    }

    @Override
    public void unregister(RegisteredListener listener) {
        globalEventBus.unregister(listener);
        listeners.remove(listener);
    }

    @Override
    public void destroy() {
        listeners.forEach(RegisteredListener::destroy);
        listeners.clear();
    }

}
