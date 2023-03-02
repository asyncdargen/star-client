package ru.starfarm.client.resource;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.resource.Resource;
import ru.starfarm.client.api.resource.ResourceHandler;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public abstract class AbstractResource implements Resource {

    protected static final Map<Class<? extends Resource>, Executor> EXECUTORS = new HashMap<>();

    protected final ResourceHandler resourceHandler;
    @Getter
    protected final URL resourceURL;
    protected final CompletableFuture<Void> loadFeature;
    protected byte[] bytes;

    private boolean destroyed;

    public AbstractResource(ResourceHandler resourceHandler, URL resourceURL, Function<URL, byte[]> resourceLoader) {
        this.resourceHandler = resourceHandler;
        this.resourceURL = resourceURL;
        loadFeature = CompletableFuture.runAsync(
                () -> {
                    long start = System.currentTimeMillis();
                    bytes = resourceLoader.apply(resourceURL);
                    StarClient.LOGGER.info("Resource loaded " + resourceURL + " took " + (System.currentTimeMillis() - start) + " ms");
                },
                EXECUTORS.computeIfAbsent(getClass(), cls -> Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()))
        );

        loadFeature.whenComplete((bytes, throwable) -> {
            if (throwable != null) {
                val cause = throwable.getCause();
                if (cause instanceof FileNotFoundException || throwable instanceof FileNotFoundException)
                    StarClient.LOGGER.error("Resource " + resourceURL + " not found!");
                else StarClient.LOGGER.error("Error while loading resource " + resourceURL, throwable);
            }
        });
    }

    @Override
    @SneakyThrows
    public void joinLoad() {
        if (!isLoaded()) loadFeature.join();
    }

    @Override
    @SneakyThrows
    public void joinLoad(long time, TimeUnit timeUnit) {
        if (!isLoaded()) loadFeature.get(time, timeUnit);
    }

    @Override
    public void destroy() {
        destroyed = true;
        loadFeature.cancel(true);
        bytes = null;
        resourceHandler.delete(this);
    }

    @Override
    public boolean isLoaded() {
        return loadFeature.isDone() && bytes != null && !destroyed;
    }

    @Override
    @SneakyThrows
    public InputStream openInputStream() {
        if (isLoaded())
            return new ByteArrayInputStream(bytes);

        throw new IllegalStateException("resource not loaded");
    }

}
