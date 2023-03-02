package ru.starfarm.client.event.listener;

import lombok.Getter;
import one.util.streamex.StreamEx;
import ru.starfarm.client.api.event.EventBus;
import ru.starfarm.client.api.event.Listener;
import ru.starfarm.client.api.event.RegisteredListener;
import ru.starfarm.client.util.Internals;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MultiMethodListener implements RegisteredListener {

    protected final EventBus eventBus;
    @Getter
    protected final List<? extends RegisteredListener> listeners;

    public MultiMethodListener(EventBus eventBus, Object... listeners) {
        this.eventBus = eventBus;
        this.listeners = StreamEx.of(listeners).map(listener -> Arrays.stream(listener.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Listener.class) && !Modifier.isStatic(method.getModifiers()))
                .map(method -> new FunctionalListener(
                        eventBus, method.getParameterTypes()[0],
                        Internals.asInterface(Consumer.class, Internals.unreflectMethod(method).bindTo(listener)))
                ).collect(Collectors.toList())
        ).<FunctionalListener, List<FunctionalListener>>toFlatCollection(list -> list, ArrayList::new);
    }

    @Override
    public void post(Object event) {
        listeners.forEach(listener -> listener.post(event));
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
        listeners.forEach(RegisteredListener::destroy);
    }

    @Override
    public Class<?> getEventClass() {
        return null;
    }
}
