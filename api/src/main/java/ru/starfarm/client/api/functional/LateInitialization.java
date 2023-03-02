package ru.starfarm.client.api.functional;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
public class LateInitialization<T> implements Lazy<T> {

    protected T value;

    @Override
    public T getValue() {
        if (value == null)
            throw new IllegalStateException("Lateinit value not initialized");

        return value;
    }

}
