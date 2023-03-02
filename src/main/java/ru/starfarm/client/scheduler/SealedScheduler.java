package ru.starfarm.client.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.starfarm.client.api.task.Scheduler;
import ru.starfarm.client.api.task.Task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class SealedScheduler implements Scheduler {


    protected final Scheduler globalScheduler;

    protected final Map<Integer, Task> tasks = new ConcurrentHashMap<>();


    @Override
    public void destroy() {
        cancelAll();
    }

    @Override
    public Task after(long delay, Consumer<Task> handler) {
        val task = globalScheduler.after(delay, handler);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task afterAsync(long delay, Consumer<Task> handler) {
        val task = globalScheduler.afterAsync(delay, handler);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task every(long delay, long period, Consumer<Task> handler) {
        val task = globalScheduler.every(delay, period, handler);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task everyAsync(long delay, long period, Consumer<Task> handler) {
        val task = globalScheduler.everyAsync(delay, period, handler);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task cancel(int taskId) {
        val task = globalScheduler.cancel(taskId);
        if (task != null) tasks.remove(taskId);
        return task;
    }

    @Override
    public Task getById(int taskId) {
        return tasks.get(taskId);
    }

    @Override
    public void cancelAll() {
        tasks.values().stream().map(Task::getId).forEach(this::cancel);
        tasks.clear();
    }

}
