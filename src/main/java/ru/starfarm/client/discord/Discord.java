package ru.starfarm.client.discord;

import com.sun.jna.Library;
import com.sun.jna.Native;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Discord {

    public final int DISCORD_REPLY_NO = 0;
    public final int DISCORD_REPLY_YES = 1;
    public final int DISCORD_REPLY_IGNORE = 2;

    public void initialize(String application, DiscordEventHandlers handlers, boolean autoRegister, String steamId) {
        DiscordRPC.INSTANCE.Discord_Initialize(application, handlers, autoRegister, steamId);
    }

    public void initialize(String application, DiscordEventHandlers handlers, boolean autoRegister) {
        initialize(application, handlers, autoRegister, null);
    }

    public void initialize(String application, DiscordEventHandlers handlers) {
        initialize(application, handlers, true);
    }

    public void shutdown() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
    }

    public void runCallbacks() {
        DiscordRPC.INSTANCE.Discord_RunCallbacks();
    }

    public void updateConnection() {
        DiscordRPC.INSTANCE.Discord_UpdateConnection();
    }

    public void updatePresence(DiscordRichPresence presence) {
        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
    }

    public void clearPresence() {
        DiscordRPC.INSTANCE.Discord_ClearPresence();
    }

    public void respond(String userId, int reply) {
        DiscordRPC.INSTANCE.Discord_Respond(userId, reply);
    }

    public void updateHandlers(DiscordEventHandlers handlers) {
        DiscordRPC.INSTANCE.Discord_UpdateHandlers(handlers);
    }

    public void register(String application, String command) {
        DiscordRPC.INSTANCE.Discord_Register(application, command);
    }

    public void registerSteamGame(String application, String steamId) {
        DiscordRPC.INSTANCE.Discord_RegisterSteamGame(application, steamId);
    }

    interface DiscordRPC extends Library {

        DiscordRPC INSTANCE = Native.loadLibrary("discord-rpc", DiscordRPC.class);

        void Discord_Initialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister, String steamId);

        void Discord_Shutdown();

        void Discord_RunCallbacks();

        void Discord_UpdateConnection();

        void Discord_UpdatePresence(DiscordRichPresence struct);

        void Discord_ClearPresence();

        void Discord_Respond(String userid, int reply);

        void Discord_UpdateHandlers(DiscordEventHandlers handlers);

        void Discord_Register(String applicationId, String command);

        void Discord_RegisterSteamGame(String applicationId, String steamId);

    }

}
