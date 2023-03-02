package ru.starfarm.client.api.render;

public interface FontRenderer {

    int height();

    int width(String text);

    void drawString(float x, float y, String text, boolean dropShadow, int color);

    void drawString(float x, float y, String text, boolean dropShadow);

    void drawStringWithShadow(float x, float y, String text, int color);

    void drawStringWithShadow(float x, float y, String text);

    void drawCenteredString(float x, float y, String text, boolean dropShadow, int color);

    void drawCenteredString(float x, float y, String text, boolean dropShadow);

    void drawCenteredStringWithShadow(float x, float y, String text, int color);

    void drawCenteredStringWithShadow(float x, float y, String text);

}
