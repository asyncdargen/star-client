package ru.starfarm.client.scheduler;

import lombok.SneakyThrows;
import lombok.val;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.task.Scheduler;
import ru.starfarm.client.api.task.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class GlobalScheduler implements Scheduler {

    protected final Thread thread = new Thread(this::run, "StarClient Scheduler Thread");
    protected final ExecutorService executor = Executors.newCachedThreadPool();
    protected final Map<Integer, Task> tasks = new ConcurrentHashMap<>();

    public GlobalScheduler() {
        thread.start();
    }

    @Override
    public void destroy() {
        tasks.clear();
    }

    @Override
    public Task after(long delay, Consumer<Task> handler) {
        val task = new LaterTask(delay, true, handler);

        tasks.put(task.getId(), task);

        return task;
    }

    @Override
    public Task afterAsync(long delay, Consumer<Task> handler) {
        val task = new LaterTask(delay, false, handler);

        tasks.put(task.getId(), task);

        return task;
    }

    @Override
    public Task every(long delay, long period, Consumer<Task> handler) {
        val task = new ScheduledTask(delay, period, true, handler);

        tasks.put(task.getId(), task);

        return task;
    }

    @Override
    public Task everyAsync(long delay, long period, Consumer<Task> handler) {
        val task = new ScheduledTask(delay, period, false, handler);

        tasks.put(task.getId(), task);

        return task;
    }

    @Override
    public Task cancel(int taskId) {
        return tasks.remove(taskId);
    }

    @Override
    public Task getById(int taskId) {
        return tasks.get(taskId);
    }

    @Override
    public void cancelAll() {
        destroy();
    }

    @SneakyThrows
    private void run() {
        while (!thread.isInterrupted()) {
            val currentTick = System.currentTimeMillis();
            tasks.values().forEach(task -> {
                if (task.isSync()) {
                    try {
                        task.tick(currentTick);
                    } catch (Throwable t) {
                        StarClient.LOGGER.error("Error while running task " + task.getId(), t);
                    }
                } else executor.execute(() -> {
                    try {
                        task.tick(currentTick);
                    } catch (Throwable t) {
                        StarClient.LOGGER.error("Error while running task " + task.getId(), t);
                    }
                });
            });
            Thread.sleep(50);
        }
    }

}
