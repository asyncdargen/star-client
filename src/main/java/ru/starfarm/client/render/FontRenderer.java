package ru.starfarm.client.render;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import ru.starfarm.client.api.functional.Lazy;

@RequiredArgsConstructor
public class FontRenderer implements ru.starfarm.client.api.render.FontRenderer {

    protected final Lazy<net.minecraft.client.gui.FontRenderer> fontRenderer = Lazy.by(() -> Minecraft.getMinecraft().fontRenderer);

    @Override
    public int height() {
        return fontRenderer.getValue().FONT_HEIGHT;
    }

    @Override
    public int width(String text) {
        return fontRenderer.getValue().getStringWidth(text);
    }

    @Override
    public void drawString(float x, float y, String text, boolean dropShadow, int color) {
        fontRenderer.getValue().drawString(text, x, y, color, dropShadow);
    }

    @Override
    public void drawString(float x, float y, String text, boolean dropShadow) {
        drawString(x, y, text, dropShadow, -1);
    }

    @Override
    public void drawStringWithShadow(float x, float y, String text, int color) {
        drawString(x, y, text, true, color);
    }

    @Override
    public void drawStringWithShadow(float x, float y, String text) {
        drawString(x, y, text, true);
    }

    @Override
    public void drawCenteredString(float x, float y, String text, boolean dropShadow, int color) {
        drawString(x - width(text) / 2f, y, text, dropShadow, color);
    }

    @Override
    public void drawCenteredString(float x, float y, String text, boolean dropShadow) {
        drawCenteredString(x, y, text, dropShadow, -1);
    }

    @Override
    public void drawCenteredStringWithShadow(float x, float y, String text, int color) {
        drawCenteredString(x, y, text, true, color);
    }

    @Override
    public void drawCenteredStringWithShadow(float x, float y, String text) {
        drawCenteredStringWithShadow(x, y, text, -1);
    }

}
