package ru.starfarm.client.mixins;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.starfarm.client.StarClient;

import java.util.concurrent.TimeUnit;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {

    private static final Cache<SPacketCustomPayload, Object> LATEST_PAYLOAD_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    @Inject(at = @At("TAIL"), method = "handleCustomPayload")
    public void handleCustomPayload(SPacketCustomPayload packet, CallbackInfo ci) {
        if (!LATEST_PAYLOAD_CACHE.asMap().containsKey(packet))
            StarClient.instance().payload().post(packet.getChannelName(), packet.getBufferData());
        LATEST_PAYLOAD_CACHE.put(packet, packet);
    }

//    @Inject(at = @At("HEAD"), method = "handleParticles", cancellable = true)
//    public void handleCustomPayload(SPacketParticles packet, CallbackInfo ci) {
////        if (packet.getParticleCount() > 500) {
//            ci.cancel();
////            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(
////                    "§cИди нахуй гриша §e" + packet.getParticleCount() + " " + packet.getParticleType()));
////        }
//    }
//    @Inject(at = @At("HEAD"), method = "handleSpawnMob", cancellable = true)
//    public void handleCustomPayload(SPacketSpawnMob packetIn, CallbackInfo ci) {
//        System.out.println(new Gson().toJson(packetIn));
//    }


}
