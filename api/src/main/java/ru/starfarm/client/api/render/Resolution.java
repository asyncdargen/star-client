package ru.starfarm.client.api.render;

public interface Resolution {

    int getWidth();

    int getHeight();

    double getWidthDouble();

    double getHeightDouble();

    int getScaleFactor();

    Resolution divide(double divider);

    Resolution multiply(double multiplier);

}
