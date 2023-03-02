package ru.starfarm.client.mixins;

import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import ru.starfarm.client.api.render.Resolution;

@Mixin(ScaledResolution.class)
public abstract class ScaledResolutionMixin implements Resolution {


    @Shadow public abstract int getScaledWidth();

    @Shadow public abstract int getScaledHeight();

    @Shadow public abstract double getScaledWidth_double();

    @Mutable
    @Shadow @Final private double scaledWidthD;

    @Mutable
    @Shadow @Final private double scaledHeightD;

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow private int scaleFactor;

    @Override
    public int getWidth() {
        return getScaledWidth();
    }

    @Override
    public int getHeight() {
        return getScaledHeight();
    }

    @Override
    public double getWidthDouble() {
        return getScaledWidth_double();
    }

    @Override
    public double getHeightDouble() {
        return getScaledWidth_double();
    }

    @Override
    public Resolution divide(double divider) {
        scaledWidthD /= divider;
        scaledHeightD /= divider;
        scaledWidth /= divider;
        scaledHeight /= divider;
        return this;
    }

    @Override
    public Resolution multiply(double multiplier) {
        scaledWidthD *= multiplier;
        scaledHeightD *= multiplier;
        scaledWidth *= multiplier;
        scaledHeight *= multiplier;
        return this;
    }

    @Override
    public int getScaleFactor() {
        return scaleFactor;
    }
}

