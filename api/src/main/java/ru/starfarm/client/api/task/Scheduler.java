package ru.starfarm.client.api.task;

import ru.starfarm.client.api.functional.Destroyable;

import java.util.function.Consumer;

public interface Scheduler extends Destroyable {

    Task after(long delay, Consumer<Task> handler);

    Task afterAsync(long delay, Consumer<Task> handler);

    Task every(long delay, long period, Consumer<Task> handler);

    Task everyAsync(long delay, long period, Consumer<Task> handler);

    Task cancel(int taskId);

    Task getById(int taskId);

    void cancelAll();

}
