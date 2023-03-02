package ru.starfarm.client.scheduler;

import ru.starfarm.client.api.task.Task;

import java.util.function.Consumer;

public class ScheduledTask extends BaseTask {

    protected long lastTickTimeMillis = 0;

    protected long delay;
    protected final long period;

    public ScheduledTask(long delay, long period, boolean sync, Consumer<Task> handler) {
        super(sync, handler);
        this.delay = delay;
        this.period = period;
    }

    @Override
    public void tick(long currentTickTimeMillis) {
        if (delay <= 0) {
            if (lastTickTimeMillis + period * 50 <= currentTickTimeMillis) {
                lastTickTimeMillis = currentTickTimeMillis;
                handler.accept(this);
            }
        } else delay--;
    }

}
