package ru.starfarm.client.api;

import ru.starfarm.client.api.event.EventBus;
import ru.starfarm.client.api.functional.Destroyable;
import ru.starfarm.client.api.payload.Payload;
import ru.starfarm.client.api.render.FontRenderer;
import ru.starfarm.client.api.render.OverlayRenderer;
import ru.starfarm.client.api.resource.ResourceHandler;
import ru.starfarm.client.api.server.StarFarm;
import ru.starfarm.client.api.task.Scheduler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public interface ClientApi extends Destroyable {

    Logger logger();

    EventBus eventBus();

    Scheduler scheduler();

    Payload payload();

    FontRenderer fontRenderer();

    OverlayRenderer overlayRenderer();

    ResourceHandler resourceHandler();

    ScheduledExecutorService executor();

    ThreadFactory threadFactory();

    StarFarm starFarm();

    Thread thread(Runnable runnable);

}
