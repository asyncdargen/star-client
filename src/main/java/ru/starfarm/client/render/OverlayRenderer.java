package ru.starfarm.client.render;

import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import ru.starfarm.client.api.render.MousePosition;
import ru.starfarm.client.api.render.Resolution;
import ru.starfarm.client.api.resource.TextureResource;

import java.util.List;
import java.util.function.Consumer;

public class OverlayRenderer implements ru.starfarm.client.api.render.OverlayRenderer {

    @Override
    public Resolution resolution() {
        return (Resolution) new ScaledResolution(Minecraft.getMinecraft());
    }

    @Override
    public MousePosition mouse() {
        return new ru.starfarm.client.render.MousePosition(resolution());
    }

    @Override
    public boolean isOpenScreen() {
        return Minecraft.getMinecraft().currentScreen != null;
    }

    @Override
    public void bindTexture(TextureResource resource) {
        resource.bind();
    }

    @Override
    public void isolateMatrix(Runnable runnable) {
        GL11.glPushMatrix();
        runnable.run();
        GL11.glPopMatrix();
    }

    @Override
    public void scaled(float scale, Consumer<Resolution> consumer) {
        isolateMatrix(() -> {
            GL11.glScalef(scale, scale, scale);
            consumer.accept(resolution().divide(scale));
        });
    }

    @Override
    public void bindSelfSkin() {
        Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().player.getLocationSkin());
    }

    @Override
    public void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    @Override
    public void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }

    @Override
    public void drawRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }

    @Override
    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    @Override
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((x + 0), (y + height), 0).tex(((float) (textureX + 0) * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((x + width), (y + height), 0).tex(((float) (textureX + width) * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((x + width), (y + 0), 0).tex(((float) (textureX + width) * 0.00390625F), ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((x + 0), (y + 0), 0).tex(((float) (textureX + 0) * 0.00390625F), ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    @Override
    public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((xCoord + 0.0F), (yCoord + (float) maxV), 0).tex(((float) (minU + 0) * 0.00390625F), ((float) (minV + maxV) * 0.00390625F)).endVertex();
        bufferbuilder.pos((xCoord + (float) maxU), (yCoord + (float) maxV), 0).tex(((float) (minU + maxU) * 0.00390625F), ((float) (minV + maxV) * 0.00390625F)).endVertex();
        bufferbuilder.pos((xCoord + (float) maxU), (yCoord + 0.0F), 0).tex(((float) (minU + maxU) * 0.00390625F), ((float) (minV + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((xCoord + 0.0F), (yCoord + 0.0F), 0).tex(((float) (minU + 0) * 0.00390625F), ((float) (minV + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    @Override
    public void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, (y + height), 0.0D).tex((u * f), ((v + (float) height) * f1)).endVertex();
        bufferbuilder.pos((x + width), (y + height), 0.0D).tex(((u + (float) width) * f), ((v + (float) height) * f1)).endVertex();
        bufferbuilder.pos((x + width), y, 0.0D).tex(((u + (float) width) * f), (v * f1)).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex((u * f), (v * f1)).endVertex();
        tessellator.draw();
    }

    @Override
    public void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, (y + height), 0.0D).tex((u * f), ((v + (float) vHeight) * f1)).endVertex();
        bufferbuilder.pos((x + width), (y + height), 0.0D).tex(((u + (float) uWidth) * f), ((v + (float) vHeight) * f1)).endVertex();
        bufferbuilder.pos((x + width), y, 0.0D).tex(((u + (float) uWidth) * f), (v * f1)).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex((u * f), (v * f1)).endVertex();
        tessellator.draw();
    }

    @Override
    public void drawHoveringText(int x, int y, List<String> text) {
        val resolution = resolution();
        int width = 0;

        for (String line : text) width = Math.max(width, Minecraft.getMinecraft().fontRenderer.getStringWidth(line));
        x += 12;
        y -= 12;
        int k = 8;

        if (text.size() > 1) k += 2 + (text.size() - 1) * 10;

        if (x + width > resolution.getWidth()) x -= 28 + width;

        if (y + k + 6 > resolution.getHeight()) y = resolution.getHeight() - k - 6;

        drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, -267386864, -267386864);
        drawGradientRect(x - 3, y + k + 3, x + width + 3, y + k + 4, -267386864, -267386864);
        drawGradientRect(x - 3, y - 3, x + width + 3, y + k + 3, -267386864, -267386864);
        drawGradientRect(x - 4, y - 3, x - 3, y + k + 3, -267386864, -267386864);
        drawGradientRect(x + width + 3, y - 3, x + width + 4, y + k + 3, -267386864, -267386864);
        drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + k + 3 - 1, 1347420415, 1344798847);
        drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + k + 3 - 1, 1347420415, 1344798847);
        drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, 1347420415, 1347420415);
        drawGradientRect(x - 3, y + k + 2, x + width + 3, y + k + 3, 1344798847, 1344798847);

        for (int i = 0; i < text.size(); ++i) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text.get(i), x, y, -1);
            y += i == 0 ? 12 : 10;
        }
    }

    @Override
    public void clear() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

}