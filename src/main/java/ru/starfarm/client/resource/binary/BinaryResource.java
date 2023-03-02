package ru.starfarm.client.resource.binary;

import ru.starfarm.client.api.resource.ResourceHandler;
import ru.starfarm.client.resource.AbstractResource;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class BinaryResource extends AbstractResource implements ru.starfarm.client.api.resource.BinaryResource {
    public BinaryResource(ResourceHandler resourceHandler, URL resourceURL, Function<URL, byte[]> resourceLoader) {
        super(resourceHandler, resourceURL, resourceLoader);
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String getString() {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
