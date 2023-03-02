package ru.starfarm.client.resource;

import lombok.RequiredArgsConstructor;
import ru.starfarm.client.api.resource.*;
import ru.starfarm.client.mod.ModClientApi;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SealedResourceHandler implements ResourceHandler {

    protected final ModClientApi modClientApi;
    protected final ResourceHandler globalResourceHandler;
    protected final List<Resource> resources = new ArrayList<>();

    @Override
    public void destroy() {
        resources.forEach(Resource::destroy);
        resources.clear();
    }

    @Override
    public ResourceGroup group() {
        return globalResourceHandler.group();
    }

    @Override
    public SoundResource soundClassPath(String path) {
        return addResource(((GlobalResourceHandler) globalResourceHandler).sound0(modClientApi.clientModData().modMainClass().getResource(path), true));
    }

    @Override
    public SoundResource soundClassPath(String path, ResourceGroup group) {
        return globalResourceHandler.soundClassPath(path, group);
    }

    @Override
    public SoundResource sound(String url) {
        return (addResource(globalResourceHandler.sound(url)));
    }

    @Override
    public SoundResource sound(String url, ResourceGroup group) {
        return globalResourceHandler.sound(url, group);
    }

    @Override
    public SoundResource sound(URL url) {
        return (addResource(globalResourceHandler.sound(url)));
    }

    @Override
    public SoundResource sound(URL url, ResourceGroup group) {
        return globalResourceHandler.sound(url, group);
    }

    @Override
    public TextureResource textureClassPath(String path) {
        return addResource(((GlobalResourceHandler) globalResourceHandler).texture0(modClientApi.clientModData().modMainClass().getResource(path), true));
    }

    @Override
    public TextureResource textureClassPath(String path, ResourceGroup group) {
        return globalResourceHandler.textureClassPath(path, group);
    }

    @Override
    public TextureResource texture(String url) {
        return addResource(globalResourceHandler.texture(url));
    }

    @Override
    public TextureResource texture(String url, ResourceGroup group) {
        return globalResourceHandler.texture(url, group);
    }

    @Override
    public TextureResource texture(URL url) {
        return addResource(globalResourceHandler.texture(url));
    }

    @Override
    public TextureResource texture(URL url, ResourceGroup group) {
        return globalResourceHandler.texture(url, group);
    }

    @Override
    public BinaryResource binaryClassPath(String path) {
        return addResource(((GlobalResourceHandler) globalResourceHandler).binary0(modClientApi.clientModData().modMainClass().getResource(path), true));
    }

    @Override
    public BinaryResource binaryClassPath(String path, ResourceGroup group) {
        return globalResourceHandler.binaryClassPath(path, group);
    }

    @Override
    public BinaryResource binary(String url) {
        return addResource(globalResourceHandler.binary(url));
    }

    @Override
    public BinaryResource binary(String url, ResourceGroup group) {
        return globalResourceHandler.binary(url, group);
    }

    @Override
    public BinaryResource binary(URL url) {
        return addResource(globalResourceHandler.binary(url));
    }

    @Override
    public BinaryResource binary(URL url, ResourceGroup group) {
        return globalResourceHandler.binary(url, group);
    }

    @Override
    public void delete(Resource resource) {
        resources.remove(resource);
        resource.destroy();
    }

    @Override
    public void delete(ResourceGroup group) {
        globalResourceHandler.delete(group);
    }

    @Override
    public <R extends Resource> R addResource(R resource) {
        resources.add(resource);
        return resource;
    }

}
