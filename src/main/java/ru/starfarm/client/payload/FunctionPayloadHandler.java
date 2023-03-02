package ru.starfarm.client.payload;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.payload.Payload;
import ru.starfarm.client.api.payload.PayloadHandler;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class FunctionPayloadHandler implements PayloadHandler {

    protected final Payload payload;
    @Getter
    protected final String channel;
    protected final Consumer<ByteBuf> handler;
    @Override
    public void destroy() {
        payload.unregister(this);
    }

    @Override
    public void handle(ByteBuf byteBuf) {
        try {
            handler.accept(byteBuf);
        } catch (Throwable throwable) {
            StarClient.LOGGER.error("Error while handling packet on channel " + channel, throwable);
        }
    }

}
