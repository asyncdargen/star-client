package ru.starfarm.client.resource;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import ru.dargen.dbcl.util.IOHelper;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.resource.*;
import ru.starfarm.client.resource.sound.DynamicSoundResource;
import ru.starfarm.client.resource.texture.DynamicTextureResource;
import ru.starfarm.client.util.FileCryptUtil;
import ru.starfarm.client.util.HashUtil;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class GlobalResourceHandler implements ResourceHandler {

    protected final boolean cacheResources = Boolean.parseBoolean(System.getProperty("client.resource.cache", "true"));
    protected final File cacheFolder = new File(StarClient.FOLDER, "cache");

    @Getter
    protected final List<Resource> resources = new ArrayList<>();

    {
        if (!cacheFolder.exists()) cacheFolder.mkdirs();
    }

    @Override
    public void destroy() {
        new ArrayList<>(resources).forEach(Resource::destroy);
        resources.clear();
    }

    @Override
    public ResourceGroup group() {
        return new SimpleResourceGroup();
    }

    @Override
    public SoundResource soundClassPath(String path) {
        return addResource(sound0(StarClient.class.getResource(path), true));
    }

    @Override
    public SoundResource soundClassPath(String path, ResourceGroup group) {
        return group.addResource(soundClassPath(path));
    }

    @Override
    @SneakyThrows
    public SoundResource sound(String url) {
        return addResource(sound0(new URL(url), false));
    }

    @Override
    public SoundResource sound(String url, ResourceGroup group) {
        return group.addResource(soundClassPath(url));
    }

    @Override
    public SoundResource sound(URL url) {
        return addResource(sound0(url, false));
    }

    @Override
    public SoundResource sound(URL url, ResourceGroup group) {
        return group.addResource(sound(url));
    }

    protected SoundResource sound0(URL url, boolean classPath) {
        return addResource(new DynamicSoundResource(this, url, classPath ? this::readURLBytes : this::loadResource));
    }

    @Override
    public TextureResource textureClassPath(String path) {
        return addResource(texture0(StarClient.class.getResource(path), true));
    }

    @Override
    public TextureResource textureClassPath(String path, ResourceGroup group) {
        return group.addResource(textureClassPath(path));
    }

    @Override
    @SneakyThrows
    public TextureResource texture(String url) {
        return addResource(texture0(new URL(url), false));
    }

    @Override
    public TextureResource texture(String url, ResourceGroup group) {
        return group.addResource(texture(url));
    }

    @Override
    public TextureResource texture(URL url) {
        return addResource(texture0(url, false));
    }

    @Override
    public TextureResource texture(URL url, ResourceGroup group) {
        return group.addResource(texture(url));
    }

    protected TextureResource texture0(URL url, boolean classPath) {
        return addResource(new DynamicTextureResource(this, url, classPath ? this::readURLBytes : this::loadResource));
    }

    @Override
    public BinaryResource binaryClassPath(String path) {
        return addResource(binary0(StarClient.class.getResource(path), true));
    }

    @Override
    public BinaryResource binaryClassPath(String path, ResourceGroup group) {
        return group.addResource(binaryClassPath(path));
    }

    @Override
    @SneakyThrows
    public BinaryResource binary(String url) {
        return addResource(binary0(new URL(url), false));
    }

    @Override
    public BinaryResource binary(String url, ResourceGroup group) {
        return group.addResource(binary(url));
    }

    @Override
    public BinaryResource binary(URL url) {
        return addResource(binary0(url, false));
    }

    @Override
    public BinaryResource binary(URL url, ResourceGroup group) {
        return group.addResource(binary(url));
    }

    protected BinaryResource binary0(URL url, boolean classPath) {
        return addResource(new ru.starfarm.client.resource.binary.BinaryResource(this, url, classPath ? this::readURLBytes : this::loadResource));
    }


    @Override
    public void delete(Resource resource) {
        resources.remove(resource);
    }

    @Override
    public void delete(ResourceGroup group) {
        group.destroy();
    }

    @Override
    public <R extends Resource> R addResource(R resource) {
        resources.add(resource);
        return resource;
    }

    @SneakyThrows
    private byte[] readURLBytes(URL url) {
        return IOHelper.readAllBytes(url.openStream());
    }

    @SneakyThrows
    private byte[] loadResource(URL url) {
        val connection = ((HttpURLConnection) url.openConnection());
        connection.addRequestProperty("User-Agent", "StarClient/1.0");
        connection.setDoInput(true);

        val hash = HashUtil.hash(url + ":" + connection.getHeaderField("last-modified"));
        val cachedFile = new File(cacheFolder, hash);

        if (cacheResources && cachedFile.exists()) try {
            val bytes = FileCryptUtil.decode(Files.newInputStream(cachedFile.toPath()));
            StarClient.LOGGER.info("Resource " + url + " loaded from cache '" + hash + "'");
            return bytes;
        } catch (Throwable throwable) {
            StarClient.LOGGER.error("Error while reading cached file " + hash, throwable);
        }

        val bytes = IOHelper.readAllBytes(connection.getInputStream());

        if (cacheResources) FileCryptUtil.encodeTo(bytes, Files.newOutputStream(cachedFile.toPath()));

        return bytes;
    }

}
