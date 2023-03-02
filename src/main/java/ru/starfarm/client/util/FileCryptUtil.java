package ru.starfarm.client.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import ru.dargen.dbcl.util.IOHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.*;

@UtilityClass
public class FileCryptUtil {

    public byte xor(byte b) {
        return (byte) (b ^ 342);
    }

    @SneakyThrows
    public static void encodeTo(byte[] bytes, OutputStream outputStream) {
        byte[] buffer = new byte[bytes.length];
        try (val deflaterOutputStream = new DeflaterOutputStream(outputStream, new Deflater(9))) {
            for (int i = 0; i < bytes.length; i++) buffer[i] = xor(bytes[i]);
            deflaterOutputStream.write(buffer);
        }
    }

    @SneakyThrows
    public byte[] decode(InputStream inputStream) {
        val inflaterInputStream = new InflaterInputStream(inputStream, new Inflater());
        byte[] bytes = IOHelper.readAllBytes(inflaterInputStream);
        for (int i = 0; i < bytes.length; i++) bytes[i] = xor(bytes[i]);
        return bytes;
    }

}
