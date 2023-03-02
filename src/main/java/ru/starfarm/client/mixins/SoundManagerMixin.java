package ru.starfarm.client.mixins;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.starfarm.client.resource.sound.SoundHook;

import java.util.Map;

@Mixin(GameSettings.class)
public class SoundManagerMixin {

    @Shadow @Final private Map<SoundCategory, Float> soundLevels;

    @Inject(at = @At("HEAD"), method = "setSoundLevel", cancellable = true)
    public void setSoundLevel(SoundCategory category, float volume, CallbackInfo ci) {
        if (category == SoundHook.CATEGORY) {
            ci.cancel();
            SoundHook.updateVolumeLevel(volume);
            soundLevels.put(category, volume);
        }
    }

}
