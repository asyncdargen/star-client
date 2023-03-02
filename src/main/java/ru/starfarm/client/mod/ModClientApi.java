package ru.starfarm.client.mod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.ClientApi;
import ru.starfarm.client.api.event.EventBus;
import ru.starfarm.client.api.payload.Payload;
import ru.starfarm.client.api.render.FontRenderer;
import ru.starfarm.client.api.render.OverlayRenderer;
import ru.starfarm.client.api.resource.ResourceHandler;
import ru.starfarm.client.api.server.StarFarm;
import ru.starfarm.client.api.task.Scheduler;
import ru.starfarm.client.event.SealedEventBus;
import ru.starfarm.client.payload.SealedPayloadRegistry;
import ru.starfarm.client.resource.SealedResourceHandler;
import ru.starfarm.client.scheduler.SealedScheduler;
import ru.starfarm.client.scheduler.SealedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;


@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ModClientApi implements ClientApi {

    protected final ClientModData clientModData;

    protected Logger logger = Logger.getLogger("ClientMod");

    protected final EventBus eventBus = new SealedEventBus(StarClient.instance().eventBus());
    protected final Scheduler scheduler = new SealedScheduler(StarClient.instance().scheduler());

    protected final Payload payload = new SealedPayloadRegistry(StarClient.instance().payload());

    protected final ThreadFactory threadFactory = new SealedThreadFactory();
    protected final ResourceHandler resourceHandler = new SealedResourceHandler(this, StarClient.instance().resourceHandler());
    protected final ScheduledExecutorService executor =
            Executors.newScheduledThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors() / 2));

    @Override
    public FontRenderer fontRenderer() {
        return StarClient.instance().fontRenderer();
    }

    @Override
    public OverlayRenderer overlayRenderer() {
        return StarClient.instance().overlayRenderer();
    }

    @Override
    public Thread thread(Runnable runnable) {
        return threadFactory.newThread(runnable);
    }

    @Override
    public StarFarm starFarm() {
        return StarClient.instance().starFarm();
    }

    @Override
    public void destroy() {
        resourceHandler.destroy();

        eventBus.destroy();
        scheduler.destroy();

        payload.destroy();

        ((SealedThreadFactory) threadFactory).destroy();
        executor.shutdownNow();
    }

}
