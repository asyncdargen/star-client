package ru.starfarm.client.mod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import ru.dargen.dbcl.DynamicByteClassLoader;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.ClientMod;
import ru.starfarm.client.api.functional.Destroyable;
import ru.starfarm.client.mod.exception.InvalidModClassException;
import ru.starfarm.client.mod.exception.LoadModException;

import java.net.URLClassLoader;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ClientModData implements Destroyable {

    protected final byte[] bytes;
    protected Class<? extends ClientMod> modMainClass;
    protected ClientMod modInstance;
    protected ModClientApi clientApi;
    protected ClientModMeta meta;
    protected URLClassLoader classLoader;

    protected void init() throws Throwable {
        classLoader = (URLClassLoader) DynamicByteClassLoader.createFromBytes(bytes, StarClient.class.getClassLoader());

        meta = ClientModMeta.fromInputStream(classLoader.getResourceAsStream("mod.properties"));

        Class<?> modClass;
        try {
            modClass = classLoader.loadClass(meta.getMain());
        } catch (ClassNotFoundException exception) {
            throw new InvalidModClassException("not found mod main class", exception);
        }

        if (!ClientMod.class.isAssignableFrom(modClass))
            throw new InvalidModClassException("mod main class not extends ClientMod");

        modMainClass = (Class<? extends ClientMod>) modClass;
        try {
            modInstance = (ClientMod) modClass.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new LoadModException("error while creating mod main instance", exception);
        }

        clientApi = new ModClientApi(this);

        Minecraft.getMinecraft().addScheduledTask(this::enable);
    }

    protected void enable() {
        try {
            StarClient.LOGGER.info("Mod " + meta.getName() + " " + meta.getVersion() + " by " + meta.getAuthor() + " enabled");
            modInstance.enable(clientApi);
        } catch (Throwable throwable) {
            StarClient.LOGGER.error("Error while enabling mod " + meta.getName() + " " + meta.getVersion(), throwable);
            StarClient.instance().modManager().disable(this);
        }
    }

    protected void disable() {
        try {
            modInstance.disable(clientApi);
        } catch (Throwable throwable) {
            StarClient.LOGGER.error("Error while disabling mod " + meta.getName() + " " + meta.getVersion(), throwable);
        }
    }

    @Override
    public void destroy() {

        if (modInstance != null) Minecraft.getMinecraft().addScheduledTask(this::disable);
        try {
            classLoader.close();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        try {
            clientApi.destroy();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (meta != null)
            StarClient.LOGGER.info("Mod " + meta.getName() + " " + meta.getVersion() + " by " + meta.getAuthor() + " disabled");
    }


}
