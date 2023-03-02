package ru.starfarm.client.gui;

import lombok.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.render.Char;
import ru.starfarm.client.api.resource.ResourceGroup;
import ru.starfarm.client.api.resource.TextureResource;
import ru.starfarm.client.api.util.Util;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
public class MainMenuGui extends GuiScreen {

    public static ResourceGroup RESOURCE_GROUP = StarClient.instance().resourceHandler().group();
    public static TextureResource BACKGROUND =
            StarClient.instance().resourceHandler().texture("https://client.starfarm.fun/resources/client/background.png", RESOURCE_GROUP);
   public static TextureResource STARFARM_ICON =
            StarClient.instance().resourceHandler().texture("https://client.starfarm.fun/resources/client/icon.png", RESOURCE_GROUP);

    private final GuiMainMenu replaced;

    @Override
    @SneakyThrows
    public void initGui() {
        RESOURCE_GROUP.joinLoad();

        FMLClientHandler.instance().setupServerList();

        int x = (int) (width * .5), y = height / 2 - 87 - 30;

        addButton(new MainGuiIconButton(
                () -> {
                    if (isShiftKeyDown()) openWebLink(Util.uri("https://starfarm.fun"));
                    else FMLClientHandler.instance().connectToServer(replaced, new ServerData("StarFarm", "play.starfarm.fun", false));
                },
                x, y + 47, 84, 84, STARFARM_ICON
        ));
        addButton(new MainGuiButton(
                () -> mc.displayGuiScreen(new GuiMultiplayer(this)),
                x, y + 120, I18n.format("menu.multiplayer"))
        );
        addButton(new MainGuiButton(
                () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings)),
                x, y + 143, I18n.format("menu.options"))
        );
        addButton(new MainGuiButton(
                () -> mc.addScheduledTask((mc::shutdown)),
                x, y + 167, I18n.format("menu.quit"))
        );


//        addButton(new MainGuiButton(
//                () -> FMLClientHandler.instance().connectToServer(replaced, new ServerData("StarFarm", "play.starfarm.fun", false)),
//                x, y, "§b§lStar§c§lFarm")
//        );
//        addButton(new MainGuiButton(
//                () -> mc.displayGuiScreen(new GuiWorldSelection(this))
//                , x, y + 23, I18n.format("menu.singleplayer"))
//        );
        addButton(new MainGuiIconButton(
                () -> openWebLink(Util.uri("https://starfarm.fun/discord")),
                width - 59, height - 13, 20, 20, Char.DISCORD.getTexture()
        ));
        addButton(new MainGuiIconButton(
                () -> openWebLink(Util.uri("https://vk.com/starfarm_fun")),
                width - 36, height - 13, 20, 20, Char.VK.getTexture()
        ));
        addButton(new MainGuiIconButton(
                () -> openWebLink(Util.uri("https://t.me/Starfarm_bot")),
                width - 13, height - 13, 20, 20, Char.TELEGRAM.getTexture()
        ));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        StarClient.instance().fontRenderer().drawStringWithShadow(4, height - 13, "StarClient 1.12.2 / " + StarClient.version());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawDefaultBackground() {
        BACKGROUND.bind();
        StarClient.instance().overlayRenderer().drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        ((MainGuiButton) button).getPerformHandler().run();
    }

    private void openWebLink(URI url) {
        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke(null);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        } catch (Throwable throwable1) {
        }
    }

    static class MainGuiButton extends GuiButton {

        protected static final int COLOR = Util.rgba(0, 0, 0, 106);

        protected static final int HOVERED_COLOR = Util.rgba(255, 255, 255, 86);

        @Getter(AccessLevel.PROTECTED)
        private final Runnable performHandler;

        public MainGuiButton(Runnable performHandler, int x, int y, String buttonText) {
            this(performHandler, x, y, 150, 20, buttonText);
        }

        public MainGuiButton(Runnable performHandler, int x, int y, int width, int height, String buttonText) {
            super(0, x, y, width, height, buttonText);
            this.performHandler = performHandler;
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return StarClient.instance().overlayRenderer().mouse().contains(x - width / 2, y - height / 2, width, height);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            val overlayRenderer = StarClient.instance().overlayRenderer();
            val fontRenderer = StarClient.instance().fontRenderer();
            hovered = mousePressed(mc, mouseX, mouseY);
            overlayRenderer.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, hovered ? HOVERED_COLOR : COLOR);
            fontRenderer.drawCenteredStringWithShadow(x, y - fontRenderer.height() / 2, displayString, -1);
        }

    }

    static class MainGuiIconButton extends MainGuiButton {


        private final TextureResource resource;
        @Setter
        private boolean background;

        private long hoverIn;

        public MainGuiIconButton(Runnable performHandler, int x, int y, int width, int height, TextureResource resource) {
            super(performHandler, x, y, width, height, "");
            this.resource = resource;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            val overlayRenderer = StarClient.instance().overlayRenderer();
            if (hovered = mousePressed(mc, mouseX, mouseY)) {
                if (hoverIn == 0) hoverIn = System.currentTimeMillis();
            } else hoverIn = 0;
            resource.bind();
            int width = hovered ? (int) (this.width * (1 + Math.min(0.2, (System.currentTimeMillis() - hoverIn) / 350.0))) : this.width,
                    height = hovered ? (int) (this.height * (1 + Math.min(0.2, (System.currentTimeMillis() - hoverIn) / 350.0))) : this.height;
            if (background)
                overlayRenderer.drawRect(x - width / 2 - 2, y - height / 2 - 2, x + width / 2 + 2, y + height / 2 + 2, hovered ? HOVERED_COLOR : COLOR);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            overlayRenderer.drawScaledCustomSizeModalRect(
                    x - width / 2, y - height / 2, 0, 0,
                    resource.width(), resource.height(), width, height, resource.width(), resource.height()
            );
        }

    }


    static class MainGuiTextButton extends MainGuiButton {

        public MainGuiTextButton(Runnable performHandler, int x, int y, String text) {
            super(performHandler, x, y, StarClient.instance().fontRenderer().width(text),
                    StarClient.instance().fontRenderer().height() + 1, text);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            hovered = mousePressed(mc, mouseX, mouseY);
            StarClient.instance().fontRenderer().drawStringWithShadow(x, y, displayString, -1);
        }

    }


}
