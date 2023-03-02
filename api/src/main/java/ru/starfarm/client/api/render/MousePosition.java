package ru.starfarm.client.api.render;

public interface MousePosition {

    int getX();

    int getY();

    boolean contains(int x, int y, int width, int height);

}
