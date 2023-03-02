package ru.starfarm.client.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import ru.starfarm.client.StarClient;

import java.io.IOException;

public class IngameMenuGui extends GuiIngameMenu {

    private boolean hubConfirmation;

    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + -16, I18n.format("menu.returnToMenu")));

        if (!this.mc.isIntegratedServerRunning()) {
            (this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
            addButton(new GuiButton(7, this.width / 2 - 100, this.height / 4 + 96 + -16, 200, 20, I18n.format("ingame.join.hub")))
                    .enabled = !StarClient.instance().starFarm().isHubServer();
        }

        addButton(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + -16, I18n.format("menu.returnToGame")));
        addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 48 + -16, 98, 20, I18n.format("menu.options")));
        addButton(new GuiButton(5, this.width / 2 + 2, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements")));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                boolean flag = this.mc.isIntegratedServerRunning();
                boolean flag1 = this.mc.isConnectedToRealms();
                button.enabled = false;
                this.mc.world.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);

                if (flag) this.mc.displayGuiScreen(new GuiMainMenu());
                else if (flag1) {
                    RealmsBridge realmsbridge = new RealmsBridge();
                    realmsbridge.switchToRealms(new GuiMainMenu());
                } else this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            case 5:
                if (this.mc.player != null)
                    this.mc.displayGuiScreen(new GuiScreenAdvancements(this.mc.player.connection.getAdvancementManager()));
                break;
            case 7:
                if (hubConfirmation) mc.player.sendChatMessage("/hub");
                else {
                    hubConfirmation = true;
                    button.displayString = I18n.format("ingame.join.confirm_hub");
                }
                break;
        }
    }

}
