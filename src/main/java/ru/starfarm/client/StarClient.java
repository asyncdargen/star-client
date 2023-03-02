package ru.starfarm.client;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import ru.starfarm.client.api.event.EventBus;
import ru.starfarm.client.api.event.server.ServerChangeEvent;
import ru.starfarm.client.api.payload.Payload;
import ru.starfarm.client.api.render.FontRenderer;
import ru.starfarm.client.api.render.OverlayRenderer;
import ru.starfarm.client.api.resource.ResourceHandler;
import ru.starfarm.client.api.server.StarFarm;
import ru.starfarm.client.api.task.Scheduler;
import ru.starfarm.client.api.util.Util;
import ru.starfarm.client.discord.DiscordRPC;
import ru.starfarm.client.event.GlobalEventBus;
import ru.starfarm.client.gui.IngameMenuGui;
import ru.starfarm.client.gui.MainMenuGui;
import ru.starfarm.client.mod.ClientModManager;
import ru.starfarm.client.payload.GlobalPayloadRegistry;
import ru.starfarm.client.render.ClientFont;
import ru.starfarm.client.resource.GlobalResourceHandler;
import ru.starfarm.client.resource.sound.SoundHook;
import ru.starfarm.client.scheduler.GlobalScheduler;
import ru.starfarm.client.util.Updater;

import java.io.File;


@Getter
@Accessors(fluent = true)
@Mod(modid = "star-client")
public class StarClient {

    @Getter
    @Accessors(fluent = true)
    @Mod.Instance("star-client")
    protected static StarClient instance;

    public static final File FOLDER = new File(System.getProperty("user.dir"), "star-client");
    public static final Logger LOGGER = LogManager.getLogger("StarClient");

    protected EventBus eventBus;
    protected Scheduler scheduler;

    protected FontRenderer fontRenderer;
    protected OverlayRenderer overlayRenderer;
    protected ResourceHandler resourceHandler;

    protected Payload payload;
    protected StarFarm starFarm;

    protected DiscordRPC discordRPC;
    protected ClientModManager modManager;

    public StarClient() {
        LOGGER.info("Loading StarClient...");
        instance = this;

        if (!FOLDER.exists()) FOLDER.mkdirs();

        resourceHandler = new GlobalResourceHandler();

        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (Updater.checkAndUpdate()) {
                LOGGER.warn("Client updated, disabling minecraft...");
                FMLCommonHandler.instance().exitJava(0, true);
            }
        });


        eventBus = new GlobalEventBus();
        scheduler = new GlobalScheduler();

        fontRenderer = new ru.starfarm.client.render.FontRenderer();
        overlayRenderer = new ru.starfarm.client.render.OverlayRenderer();

        payload = new GlobalPayloadRegistry();
        starFarm = new StarFarmServer();

        discordRPC = new DiscordRPC();
        modManager = new ClientModManager();

        SoundHook.inject();

        initEvents();
        initTasks();

        ClientFont.load();

        LOGGER.info("StarClient loaded!");
    }

    private void initEvents() {
        payload.register("MC|Brand", buffer -> eventBus.fire(new ServerChangeEvent()));
        payload.register("minecraft:brand", buffer -> eventBus.fire(new ServerChangeEvent()));

        eventBus.register(GuiOpenEvent.class, event -> {
            var gui = event.getGui();

            if (gui instanceof GuiIngameMenu && !(gui instanceof IngameMenuGui)) gui = new IngameMenuGui();
            else if (gui instanceof GuiMainMenu) gui = new MainMenuGui((GuiMainMenu) gui);

            event.setGui(gui);
        });
    }

    private void initTasks() {
        StarClient.instance().scheduler().everyAsync(20, 20, task -> {
            String status;
            if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().getCurrentServerData() == null)
                status = "В меню";
            else if (StarClient.instance().starFarm().isOnServer()) {
                if (StarClient.instance().starFarm().serverName() != null)
                    status = Util.stripColor(StarClient.instance().starFarm().serverName());
                else status = "StarFarm";
            } else status = "В неизвестности";

            Minecraft.getMinecraft().addScheduledTask(() -> Display.setTitle("StarClient | " + status));
            discordRPC.update(status);
        });

    }


    public static ModContainer modContainer() {
        return Loader.instance().getIndexedModList().get("star-client");
    }

    public static String version() {
        return modContainer().getVersion();
    }

}
