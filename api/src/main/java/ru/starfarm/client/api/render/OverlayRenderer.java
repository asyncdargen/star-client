package ru.starfarm.client.api.render;

import ru.starfarm.client.api.resource.TextureResource;

import java.util.List;
import java.util.function.Consumer;

public interface OverlayRenderer {

    Resolution resolution();

    MousePosition mouse();

    boolean isOpenScreen();

    void bindTexture(TextureResource resource);

    void bindSelfSkin();

    void isolateMatrix(Runnable runnable);

    void scaled(float scale, Consumer<Resolution> consumer);

    void drawHorizontalLine(int startX, int endX, int y, int color);

    void drawVerticalLine(int x, int startY, int endY, int color);

    void drawRect(int left, int top, int right, int bottom, int color);

    void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor);

    void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height);

    void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV);

    void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight);

    void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight);

    void drawHoveringText(int x, int y, List<String> text);

    void clear();

}
