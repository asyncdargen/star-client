package ru.starfarm.client.scheduler;

import ru.starfarm.client.api.task.Task;

import java.util.function.Consumer;

public class LaterTask extends BaseTask {

    protected final long createdTickTimeMillis = System.currentTimeMillis();
    protected final long delay;

    public LaterTask(long delay, boolean sync, Consumer<Task> handler) {
        super(sync, handler);
        this.delay = delay;
    }

    @Override
    public void tick(long currentTickTimeMillis) {
        if (createdTickTimeMillis + delay * 50 >= currentTickTimeMillis) {
            cancel();
            handler.accept(this);
        }
    }

}
