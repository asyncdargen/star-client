package ru.starfarm.client.api.payload;

import io.netty.buffer.ByteBuf;
import ru.starfarm.client.api.functional.Destroyable;

public interface PayloadHandler extends Destroyable {

    String getChannel();

    void handle(ByteBuf byteBuf);

}
