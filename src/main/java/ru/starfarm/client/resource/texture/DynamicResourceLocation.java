package ru.starfarm.client.resource.texture;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;

@Getter
public class DynamicResourceLocation extends ResourceLocation {

    protected DynamicTextureResource resource;

    public DynamicResourceLocation(DynamicTextureResource resource) {
        super("star-client", resource.getResourceURL().toString());
    }

}
