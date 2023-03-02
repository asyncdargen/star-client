package ru.starfarm.client.api.functional;

import java.util.function.Supplier;

public interface Lazy<T> {

    T getValue();

    static <T> LazyInitialization<T> by(Supplier<T> initializer) {
        return new LazyInitialization<>(initializer);
    }

    static <T> LateInitialization<T> late() {
        return new LateInitialization<>();
    }

}
