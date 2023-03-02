package ru.starfarm.client.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.task.Task;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public abstract class BaseTask implements Task {

    public static final AtomicInteger TASK_ID = new AtomicInteger();

    protected final int id = TASK_ID.incrementAndGet();
    protected final boolean sync;
    protected final Consumer<Task> handler;

    @Override
    public boolean isCancelled() {
        return StarClient.instance().scheduler().getById(id) == null;
    }

    @Override
    public boolean cancel() {
        return StarClient.instance().scheduler().cancel(id) != null;
    }

}
