package ru.starfarm.client.api.task;

import java.util.function.Consumer;

public interface Task {

    int getId();

    boolean isSync();

    Consumer<Task> getHandler();

    void tick(long currentTickTimeMillis);

    boolean isCancelled();

    boolean cancel();

}
