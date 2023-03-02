package ru.starfarm.client.payload;

import io.netty.buffer.ByteBuf;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.starfarm.client.api.payload.Payload;
import ru.starfarm.client.api.payload.PayloadHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class SealedPayloadRegistry implements Payload {

    protected final Payload globalPayloadRegistry;
    protected final List<PayloadHandler> handlers = new ArrayList<>();

    @Override
    public void destroy() {
        handlers.forEach(globalPayloadRegistry::unregister);
        handlers.clear();
    }

    @Override
    public void post(String channel, ByteBuf buffer) {
        globalPayloadRegistry.post(channel, buffer);
    }

    @Override
    public void send(String channel, ByteBuf buffer) {
        globalPayloadRegistry.send(channel, buffer);
    }

    @Override
    public void send(String channel, Consumer<ByteBuf> writer) {
        globalPayloadRegistry.send(channel, writer);
    }

    @Override
    public void sendEmpty(String channel) {
        globalPayloadRegistry.sendEmpty(channel);
    }

    @Override
    public PayloadHandler register(String channel, Consumer<ByteBuf> reader) {
        val handler = globalPayloadRegistry.register(channel, reader);
        handlers.add(handler);
        return handler;
    }

    @Override
    public void unregister(String channel) {
        if (handlers.removeIf(handler -> handler.getChannel().equals(channel)))
            globalPayloadRegistry.unregister(channel);
    }

    @Override
    public void unregister(PayloadHandler handler) {
        if (handlers.remove(handler))
            globalPayloadRegistry.unregister(handler);
    }

}
