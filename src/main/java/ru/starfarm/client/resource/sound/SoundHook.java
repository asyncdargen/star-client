package ru.starfarm.client.resource.sound;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.SoundCategory;
import paulscode.sound.SoundSystem;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.functional.Lazy;
import ru.starfarm.client.mixins.SoundHandlerAccessor;
import ru.starfarm.client.resource.GlobalResourceHandler;
import ru.starfarm.client.util.Internals;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.Map;

@UtilityClass
public class SoundHook {

    public final String CATEGORY_NAME = "client";
    public SoundCategory CATEGORY;

    private static final Lazy<MethodHandle> MH_SOUND_SYSTEM_GET = Lazy.by(() ->
            Internals.findGetter(SoundManager.class, "field_148620_e")
                    .bindTo(((SoundHandlerAccessor) Minecraft.getMinecraft().getSoundHandler()).getSoundManager()));

    @SneakyThrows
    public static SoundSystem getSoundSystem() {
        return (SoundSystem) MH_SOUND_SYSTEM_GET.getValue().invoke();
    }

    @SneakyThrows
    public void inject() {
        injectSoundCategory();
        injectNewSoundVolumesSettingsMap();
    }

    @SneakyThrows
    private void injectNewSoundVolumesSettingsMap() {
        val settings = Minecraft.getMinecraft().gameSettings;
        Internals.findSetter(GameSettings.class, "field_186714_aM", Map.class).invoke(settings, Maps.newHashMap()/*newEnumMap(SoundCategory.class)*/);
        settings.loadOptions();
    }

    @SneakyThrows
    private void injectSoundCategory() {
        val newConstantOrdinal = SoundCategory.values().length;

        //Create category instance
        CATEGORY = Internals.allocateInstance(SoundCategory.class);

        //Configure base enum constant
        Internals.findSetter(Enum.class, "name", String.class).invoke(CATEGORY, CATEGORY_NAME.toUpperCase());
        Internals.findSetter(Enum.class, "ordinal", int.class).invoke(CATEGORY, newConstantOrdinal);

        //Configure enum
        Internals.findSetter(SoundCategory.class, "field_187962_l", String.class).invoke(CATEGORY, CATEGORY_NAME);

        //Adding to values
        val newCategoryValuesArray = Arrays.copyOf(SoundCategory.values(), newConstantOrdinal + 1);
        newCategoryValuesArray[newConstantOrdinal] = CATEGORY;

        Internals.UNSAFE.putObject(
                Internals.UNSAFE.staticFieldBase(SoundCategory.class),
                Internals.UNSAFE.staticFieldOffset(SoundCategory.class.getDeclaredField("$VALUES")),
                newCategoryValuesArray
        );

        //Inject category to pool
        val categoriesPool = Internals.<Map<String, SoundCategory>>getFieldValue(SoundCategory.class, null, "field_187961_k");
        categoriesPool.put(CATEGORY_NAME, CATEGORY);
    }

    public float getVolumeLevel() {
        return Minecraft.getMinecraft().gameSettings.getSoundLevel(CATEGORY);
    }

    public void updateVolumeLevel(float volume) {
        val soundSystem = getSoundSystem();
        val resourceHandler = ((GlobalResourceHandler) StarClient.instance().resourceHandler());
        resourceHandler.getResources()
                .stream()
                .filter(resource -> resource instanceof DynamicSoundResource)
                .forEach(resource -> soundSystem.setVolume(((DynamicSoundResource) resource).id(), volume));
    }

}
