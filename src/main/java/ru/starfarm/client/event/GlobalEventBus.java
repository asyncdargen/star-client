package ru.starfarm.client.event;

import io.netty.util.internal.ConcurrentSet;
import lombok.SneakyThrows;
import lombok.val;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import one.util.streamex.StreamEx;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.event.CancellableEvent;
import ru.starfarm.client.api.event.EventBus;
import ru.starfarm.client.api.event.RegisteredListener;
import ru.starfarm.client.event.listener.FunctionalListener;
import ru.starfarm.client.event.listener.MultiMethodListener;
import ru.starfarm.client.util.Internals;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GlobalEventBus implements EventBus {

    protected final Map<Class<?>, Set<RegisteredListener>> listeners = new ConcurrentHashMap<>();

    @SneakyThrows
    public GlobalEventBus() {
        val eventBusHandleMethod = net.minecraftforge.fml.common.eventhandler.EventBus.class.getDeclaredMethod(
                "register", Class.class, Object.class, Method.class, ModContainer.class
        );
        eventBusHandleMethod.setAccessible(true);
        eventBusHandleMethod.invoke(
                MinecraftForge.EVENT_BUS,
                Event.class, this,
                getClass().getDeclaredMethod("postMinecraft", Event.class),
                StarClient.modContainer()
        );
        eventBusHandleMethod.setAccessible(false);

    }

    @SubscribeEvent
    public void postMinecraft(Event event) {
        this.fire(event);
    }

    protected RegisteredListener registerListener(RegisteredListener listener) {
        listeners.computeIfAbsent(listener.getEventClass(), ec -> new ConcurrentSet<>()).add(listener);
        return listener;
    }

    @Override
    public <E> E fire(E event) {
        Class<E> eventClass = (Class<E>) event.getClass();
        listeners.getOrDefault(eventClass, Collections.emptySet()).forEach(listener -> listener.post(event));
        return event;
    }

    @Override
    public boolean fireGetResult(Object event) {
        fire(event);
        return event instanceof CancellableEvent && !((CancellableEvent) event).isCancelled();
    }

    @Override
    public RegisteredListener register(Object... listenerObjects) {
        val multiListener = new MultiMethodListener(this, listenerObjects);
        multiListener.getListeners().forEach(this::registerListener);
        return multiListener;
    }

    @Override
    public RegisteredListener register(Class<?>... listenerClasses) {
        return register(StreamEx.of(listenerClasses).map(Internals::newInstance).toArray());
    }

    @Override
    public <E> RegisteredListener register(Class<E> eventClass, Consumer<E> handler) {
        return registerListener(new FunctionalListener(this, eventClass, handler));
    }

    @Override
    public void unregister(RegisteredListener listener) {
        this.listeners.values().forEach(listeners -> listeners.removeIf(listener1 -> listener1 == listener));
    }

    @Override
    public void destroy() {
        listeners.clear();
    }

}
