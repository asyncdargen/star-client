package ru.starfarm.client.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.functional.Destroyable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

@RequiredArgsConstructor
public class SealedThreadFactory implements ThreadFactory, Destroyable {

    protected final List<Thread> threads = new ArrayList<>();

    @Override
    public Thread newThread(Runnable runnable) {
        val thread = new Thread(runnable);
        thread.setUncaughtExceptionHandler((t, exception) -> StarClient.LOGGER.error("Error in thread " + t.getName(), exception));
        threads.add(thread);
        return thread;
    }

    @Override
    public void destroy() {
        threads.forEach(Thread::interrupt);
        threads.clear();
    }

}
