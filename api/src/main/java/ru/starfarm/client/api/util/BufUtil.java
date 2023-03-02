package ru.starfarm.client.api.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class BufUtil {

    public ByteBuf buffer() {
        return Unpooled.buffer();
    }

    public void writeVarInt(ByteBuf buffer, int value) {
        while ((value & -128) != 0) {
            buffer.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        buffer.writeByte(value);
    }

    public int readVarInt(ByteBuf buffer) {
        int i = 0;
        int j = 0;

        while (true) {
            byte b0 = buffer.readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }

            if ((b0 & 128) != 128) {
                break;
            }
        }

        return i;
    }


    public void writeVarLong(ByteBuf buffer, long value) {
        while ((value & -128L) != 0L) {
            buffer.writeByte((int) (value & 127L) | 128);
            value >>>= 7;
        }

        buffer.writeByte((int) value);
    }

    public long readVarLong(ByteBuf buffer) {
        long i = 0L;
        int j = 0;

        while (true) {
            byte b0 = buffer.readByte();
            i |= (long) (b0 & 127) << j++ * 7;

            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }

            if ((b0 & 128) != 128) {
                break;
            }
        }

        return i;
    }

    public void writeInt(ByteBuf buffer, int value) {
        buffer.writeInt(value);
    }

    public int readInt(ByteBuf buffer) {
        return buffer.readInt();
    }

    public void writeLong(ByteBuf buffer, long value) {
        buffer.writeLong(value);
    }

    public long readLong(ByteBuf buffer) {
        return buffer.readLong();
    }

    public void writeDouble(ByteBuf buffer, double value) {
        buffer.writeDouble(value);
    }

    public double readDouble(ByteBuf buffer) {
        return buffer.readDouble();
    }

    public void writeByteArray(ByteBuf buffer, byte[] bytes) {
        writeVarInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
    }

    public byte[] readByteArray(ByteBuf buffer) {
        byte[] bytes = new byte[readVarInt(buffer)];
        buffer.readBytes(bytes);
        return bytes;
    }

    @SneakyThrows
    public void writeString(ByteBuf buffer, String string, String charset) {
        writeByteArray(buffer, string.getBytes(charset));
    }

    @SneakyThrows
    public String readString(ByteBuf buffer, String charset) {
        return new String(readByteArray(buffer), charset);
    }

    public void writeStringUTF8(ByteBuf buffer, String string) {
        writeString(buffer, string, "UTF-8");
    }

    public String readStringUTF8(ByteBuf buffer) {
        return readString(buffer, "UTF-8");
    }

    public void writeUUID(ByteBuf buffer, UUID uuid) {
        writeVarLong(buffer, uuid.getMostSignificantBits());
        writeVarLong(buffer, uuid.getLeastSignificantBits());
    }

    public UUID readUUID(ByteBuf buffer) {
        return new UUID(readVarLong(buffer), readVarLong(buffer));
    }

    public void writeBoolean(ByteBuf byteBuf, boolean value) {
        byteBuf.writeBoolean(value);
    }

    public boolean readBoolean(ByteBuf byteBuf) {
        return byteBuf.readBoolean();
    }

}
