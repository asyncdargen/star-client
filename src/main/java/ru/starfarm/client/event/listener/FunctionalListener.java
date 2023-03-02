package ru.starfarm.client.event.listener;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.event.EventBus;
import ru.starfarm.client.api.event.RegisteredListener;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class FunctionalListener implements RegisteredListener {

    protected final EventBus eventBus;
    @Getter
    protected final Class<?> eventClass;
    protected final Consumer<?> eventConsumer;

    @Override
    public void post(Object event) {
        try {
            if (eventClass.isAssignableFrom(event.getClass()))
                ((Consumer<Object>) eventConsumer).accept(event);
        } catch (Throwable throwable) {
            StarClient.LOGGER.error("Error while handling event " + eventClass.getName(), throwable);
        }
    }

    @Override
    public void destroy() {
        eventBus.unregister(this);
    }

}
