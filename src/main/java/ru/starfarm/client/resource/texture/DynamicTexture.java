package ru.starfarm.client.resource.texture;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import ru.starfarm.client.StarClient;

import java.io.IOException;

@RequiredArgsConstructor
public class DynamicTexture extends AbstractTexture {

    protected final DynamicTextureResource resource;

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        deleteGlTexture();

        try (val inputStream = resource.openInputStream()) {
            val image = TextureUtil.readBufferedImage(inputStream);
            resource.width = image.getWidth();
            resource.height = image.getWidth();

            TextureUtil.uploadTextureImageAllocate(getGlTextureId(), image, false, false);
        } catch (Throwable throwable) {
            StarClient.LOGGER.error("Error while loading texture " + resource.getResourceURL(), throwable);
        }
    }

}
