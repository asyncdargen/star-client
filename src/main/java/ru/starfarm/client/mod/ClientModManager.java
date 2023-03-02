package ru.starfarm.client.mod;

import lombok.val;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.event.server.ServerChangeEvent;
import ru.starfarm.client.api.functional.Destroyable;
import ru.starfarm.client.api.util.BufUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientModManager implements Destroyable {

    protected final boolean dumpMods = Boolean.parseBoolean(System.getProperty("client.mod.dump"));
    protected final File dumpModsFolder = new File(StarClient.FOLDER, "mod-dumps");

    protected final Map<String, ClientModData> loadedMods = new ConcurrentHashMap<>();

    public ClientModManager() {
        if (!dumpModsFolder.exists()) dumpModsFolder.mkdirs();

        StarClient.instance().payload().register("sf:mod", buffer -> {
            val bytes = BufUtil.readByteArray(buffer);

//            for (int i = 0; i < bytes.length; i++) bytes[i] = (byte) (bytes[i] ^ 98);

            ClientModData modData = null;
            try {

                modData = new ClientModData(bytes);

                modData.init();

                if (isLoaded(modData)) disable(modData.meta().getName());

                if (dumpMods) try (val dumpOutputStream = new FileOutputStream(new File(
                        dumpModsFolder, modData.meta().getName() + "-" + modData.meta().getVersion() + ".jar"
                ))) {
                    dumpOutputStream.write(bytes);
                }

                loadedMods.put(modData.meta().getName(), modData);
            } catch (Throwable throwable) {
                if (modData != null) modData.destroy();
                StarClient.LOGGER.error("Error while loading and enabling mod", throwable);
            }
        });

        StarClient.instance().eventBus().register(ServerChangeEvent.class, event -> destroy());
        StarClient.instance().eventBus().register(FMLNetworkEvent.ClientDisconnectionFromServerEvent.class, event -> destroy());
    }

    @Override
    public void destroy() {
        loadedMods.values().forEach(this::disable);
    }

    public void disable(String name) {
        disable(loadedMods.get(name));
    }

    public void disable(ClientModData modData) {
        if (modData != null) {
            modData.destroy();
            if (modData.meta() != null && loadedMods.get(modData.meta().getName()) == modData)
                loadedMods.remove(modData.meta().getName());
        }
    }

    public boolean isLoaded(String name) {
        return loadedMods.containsKey(name);
    }

    public boolean isLoaded(ClientModData modData) {
        return isLoaded(modData.meta().getName());
    }

}
