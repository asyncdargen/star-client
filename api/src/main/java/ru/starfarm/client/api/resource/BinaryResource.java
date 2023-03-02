package ru.starfarm.client.api.resource;

public interface BinaryResource extends Resource {

    byte[] getBytes();

    String getString();

}
