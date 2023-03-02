package ru.starfarm.client.api.functional;

import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class LazyInitialization<T> implements Lazy<T> {

    protected final Supplier<T> initializer;
    protected T value;

    @Override
    public T getValue() {
        return value == null ? (value = initializer.get()) : value;
    }

}
