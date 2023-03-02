package ru.starfarm.client.mixins.font;

import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextComponentString.class)
public class TextComponentMixin {

    @Mutable
    @Final
    @Shadow private String text;

    @Inject(at = @At("TAIL"), method = "<init>", cancellable = true)
    public void init(String inputText, CallbackInfo ci) {
        ci.cancel();
//        text = ClientFont.resolveEmojis(inputText);
    }

}
