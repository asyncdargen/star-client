package ru.starfarm.client.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.render.Char;

@Mixin(NetworkPlayerInfo.class)
public class NetworkPlayerInfoMixin {

    @Shadow private ITextComponent displayName;

    @Shadow @Final private GameProfile gameProfile;

    @Inject(at = @At("HEAD"), method = "getDisplayName", cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<ITextComponent> cir) {
        if (StarClient.instance().starFarm().isClientPlayer(gameProfile.getId())) {
            cir.setReturnValue(new TextComponentString(Char.STARFARM + " ")
                    .appendSibling(displayName == null ? new TextComponentString(gameProfile.getName()) : displayName));
        }
    }

}
