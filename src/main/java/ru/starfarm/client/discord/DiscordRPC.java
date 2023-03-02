package ru.starfarm.client.discord;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import ru.starfarm.client.StarClient;

import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class DiscordRPC {

    public static final String APPLICATION = "905105141466808401";

    protected final AtomicBoolean active = new AtomicBoolean(true);
    protected final DiscordEventHandlers handlers = new DiscordEventHandlers();
    protected final DiscordRichPresence presence = new DiscordRichPresence();
    protected final Thread thread = new Thread(this::run, "Discord-Updater-Thread");

    public DiscordRPC() {
        try {
            StarClient.LOGGER.info("Initializing discord RPC...");
            handlers.errored = (errorCode, message) -> StarClient.LOGGER
                    .error("Error (" + errorCode + ")  while initialize discord rpc: " + message);
            handlers.ready = user -> StarClient.LOGGER
                    .info("Discord initialized " + user.username + "#" + user.discriminator + " (" + user.userId + ")");
            Discord.initialize(APPLICATION, handlers);

            presence.startTimestamp = System.currentTimeMillis() / 1000;
            //Image
            presence.largeImageKey = "main";
            presence.largeImageText = "StarClient " + StarClient.version();
            presence.details = Minecraft.getMinecraft().getSession().getUsername();

            thread.start();
        } catch (Throwable t) {
            StarClient.LOGGER.error("Error while initialize discord rpc", t);
        }
    }

    public boolean isActive() {
        return active.get();
    }

//    @Override
//    public void hide() {
//        if (!isActive()) return;
//        active.set(false);
//        Discord.clearPresence();
//    }
//
//    @Override
//    public void show() {
//        active.set(true);
//    }
//

    public void update(String line) {
        presence.state = line;
    }


    @SneakyThrows
    protected void run() {
        while (!thread.isInterrupted()) {
            try {
                Discord.runCallbacks();
                if (isActive()) Discord.updatePresence(presence);
            } catch (Throwable t) {
                StarClient.LOGGER.error("Error while update discord rpc", t);
            }
            Thread.sleep(2000);
        }
    }

}
