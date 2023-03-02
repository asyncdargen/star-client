package ru.starfarm.client.api.payload;

import io.netty.buffer.ByteBuf;
import ru.starfarm.client.api.functional.Destroyable;

import java.util.function.Consumer;

public interface Payload extends Destroyable {

    void post(String channel, ByteBuf buffer);

    void send(String channel, ByteBuf buffer);

    void send(String channel, Consumer<ByteBuf> writer);

    void sendEmpty(String channel);

    PayloadHandler register(String channel, Consumer<ByteBuf> reader);

    void unregister(String channel);

    void unregister(PayloadHandler handler);

}
