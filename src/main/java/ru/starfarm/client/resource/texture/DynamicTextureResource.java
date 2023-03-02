package ru.starfarm.client.resource.texture;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.resource.ResourceHandler;
import ru.starfarm.client.api.resource.TextureResource;
import ru.starfarm.client.resource.AbstractResource;

import java.net.URL;
import java.util.function.Function;

public class DynamicTextureResource extends AbstractResource implements TextureResource {

    @Getter
    @Accessors(fluent = true)
    protected int width, height;

    protected final DynamicResourceLocation resourceLocation;
    private boolean uploaded;

    public DynamicTextureResource(ResourceHandler resourceHandler, URL resource, Function<URL, byte[]> resourceLoader) {
        super(resourceHandler, resource, resourceLoader);
        resourceLocation = new DynamicResourceLocation(this);
        loadFeature.thenAccept(__ -> Minecraft.getMinecraft().addScheduledTask(() -> {
            uploaded = true;
            Minecraft.getMinecraft().renderEngine.loadTexture(resourceLocation, new DynamicTexture(this));
            StarClient.LOGGER.info("Resource " + resource + " loaded to graphic");
        }));
    }

    @Override
    public void destroy() {
        super.destroy();
        uploaded = false;
        Minecraft.getMinecraft().addScheduledTask(() ->
                Minecraft.getMinecraft().renderEngine.deleteTexture(resourceLocation));
    }

    @Override
    public boolean isLoaded() {
        return super.isLoaded() && uploaded;
    }

    @Override
    public void bind() {
        if (isLoaded())
            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }

}
