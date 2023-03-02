package ru.starfarm.client.api.server;

import java.util.UUID;

public interface StarFarm {

    RealmId serverId();

    String serverName();

    boolean isOnServer();

    boolean isHubServer();

    boolean isClientPlayer(UUID uuid);

}
