package ru.starfarm.client.render;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import ru.starfarm.client.api.render.Resolution;

@Getter
public class MousePosition implements ru.starfarm.client.api.render.MousePosition {

    protected final int x, y;

    public MousePosition(Resolution resolution) {
        x = Mouse.getX() * resolution.getWidth() / Minecraft.getMinecraft().displayWidth;
        y = resolution.getHeight() - Mouse.getY() * resolution.getHeight() / Minecraft.getMinecraft().displayHeight;
    }

    @Override
    public boolean contains(int x, int y, int width, int height) {
        return this.x >= x && this.x <= x + width && this.y >= y && this.y <= y + height;
    }

}
