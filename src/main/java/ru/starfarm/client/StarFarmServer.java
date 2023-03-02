package ru.starfarm.client;

import io.netty.buffer.EmptyByteBuf;
import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ru.starfarm.client.api.event.server.ServerChangeEvent;
import ru.starfarm.client.api.server.RealmId;
import ru.starfarm.client.api.server.StarFarm;
import ru.starfarm.client.api.util.BufUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public class StarFarmServer implements StarFarm {

    protected RealmId serverId;
    protected String serverName;

    protected final Set<UUID> clientPlayers = new ConcurrentSet<>();

    public StarFarmServer() {
        val payload = StarClient.instance().payload();
        payload.register("sf:realm",
                buffer -> serverId = new RealmId(BufUtil.readStringUTF8(buffer), BufUtil.readInt(buffer)));
        payload.register("sf:display",
                buffer -> serverName = BufUtil.readStringUTF8(buffer));
        payload.register("sf:icall", buffer -> payload.sendEmpty("sf:init"));
        payload.register("sf:players", buffer -> {
            if (buffer instanceof EmptyByteBuf) return;
            try{
                val count = BufUtil.readVarInt(buffer);
                val players = new HashSet<UUID>();
                for (int i = 0; i < count; i++) players.add(BufUtil.readUUID(buffer));
                clientPlayers.clear();
                clientPlayers.addAll(players);
            } catch (Throwable throwable) {}
        });

        StarClient.instance().eventBus().register(ServerChangeEvent.class, event -> reset());
        StarClient.instance().eventBus().register(FMLNetworkEvent.ServerDisconnectionFromClientEvent.class, event -> reset());
    }

    public void reset() {
        serverId = null;
        serverName = null;
        clientPlayers.clear();
    }

    @Override
    public boolean isOnServer() {
        return serverId != null;
    }

    @Override
    public boolean isHubServer() {
        return isOnServer() && serverId.getName().equals("HUB");
    }

    @Override
    public boolean isClientPlayer(UUID uuid) {
        return clientPlayers.contains(uuid);
    }

}
