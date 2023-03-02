package ru.starfarm.client.api.resource;

import ru.starfarm.client.api.functional.Destroyable;

import java.net.URL;

public interface ResourceHandler extends Destroyable {

    ResourceGroup group();

    SoundResource soundClassPath(String path);

    SoundResource soundClassPath(String path, ResourceGroup group);

    SoundResource sound(String url);

    SoundResource sound(String url, ResourceGroup group);

    SoundResource sound(URL url);

    SoundResource sound(URL url, ResourceGroup group);

    TextureResource textureClassPath(String path);

    TextureResource textureClassPath(String path, ResourceGroup group);

    TextureResource texture(String url);

    TextureResource texture(String url, ResourceGroup group);

    TextureResource texture(URL url);

    TextureResource texture(URL url, ResourceGroup group);

    BinaryResource binaryClassPath(String path);

    BinaryResource binaryClassPath(String path, ResourceGroup group);

    BinaryResource binary(String url);

    BinaryResource binary(String url, ResourceGroup group);

    BinaryResource binary(URL url);

    BinaryResource binary(URL url, ResourceGroup group);

    void delete(Resource resource);

    void delete(ResourceGroup group);

    <R extends Resource> R addResource(R resource);

}
