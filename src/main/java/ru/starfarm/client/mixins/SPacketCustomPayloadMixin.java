package ru.starfarm.client.mixins;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SPacketCustomPayload.class)
public class SPacketCustomPayloadMixin {

    @Shadow
    private String channel;

    @Shadow private PacketBuffer data;

    @Inject(at = @At("HEAD"), method = "readPacketData", cancellable = true)
    public void readPacketData(PacketBuffer buffer, CallbackInfo ci) {
        ci.cancel();

        channel = buffer.readString(20);
        data = new PacketBuffer(buffer.readBytes(buffer.readableBytes()));
    }

}
