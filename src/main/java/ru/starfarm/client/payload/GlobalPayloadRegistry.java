package ru.starfarm.client.payload;

import io.netty.buffer.ByteBuf;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import ru.starfarm.client.api.util.BufUtil;
import ru.starfarm.client.api.payload.Payload;
import ru.starfarm.client.api.payload.PayloadHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GlobalPayloadRegistry implements Payload {

    protected final Map<String, PayloadHandler> handlers = new HashMap<>();

    @Override
    public void destroy() {
        handlers.clear();
    }

    @Override
    public void post(String channel, ByteBuf buffer) {
        if (handlers.get(channel) != null) handlers.get(channel).handle(buffer);
    }

    @Override
    public void send(String channel, ByteBuf buffer) {
        Minecraft.getMinecraft().player.connection
                .sendPacket(new CPacketCustomPayload(channel, new PacketBuffer(buffer)));
    }

    @Override
    public void send(String channel, Consumer<ByteBuf> writer) {
        val buffer = BufUtil.buffer();
        writer.accept(buffer);
        send(channel, buffer);
    }

    @Override
    public void sendEmpty(String channel) {
        send(channel, BufUtil.buffer());
    }

    @Override
    public PayloadHandler register(String channel, Consumer<ByteBuf> reader) {
        val handler = new FunctionPayloadHandler(this, channel, reader);
        handlers.put(channel, handler);
        return handler;
    }

    @Override
    public void unregister(String channel) {
        handlers.remove(channel);
    }

    @Override
    public void unregister(PayloadHandler handler) {
        handlers.values().remove(handler);
    }

}
