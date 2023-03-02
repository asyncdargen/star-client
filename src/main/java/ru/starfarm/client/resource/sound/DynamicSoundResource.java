package ru.starfarm.client.resource.sound;

import lombok.SneakyThrows;
import ru.starfarm.client.api.resource.ResourceHandler;
import ru.starfarm.client.api.resource.SoundResource;
import ru.starfarm.client.resource.AbstractResource;

import java.net.URL;
import java.util.UUID;
import java.util.function.Function;

public class DynamicSoundResource extends AbstractResource implements SoundResource {

    protected final UUID soundId = UUID.randomUUID();

    @SneakyThrows
    public DynamicSoundResource(ResourceHandler resourceHandler, URL resourceURL, Function<URL, byte[]> resourceLoader) {
        super(resourceHandler, resourceURL, resourceLoader);
        SoundHook.getSoundSystem().newStreamingSource(
                false, id(),
                new URL(null, "dynamic:" + id(), new DynamicSoundURLHandler(this)),
                resourceURL.toString(), false, 0, 0, 0, 0, 7F
        );
    }

    @Override
    public void destroy() {
        stop();
        SoundHook.getSoundSystem().removeSource(id());
        super.destroy();
    }

    @Override
    public void play() {
        if (isLoaded()) {
            SoundHook.getSoundSystem().play(id());
            SoundHook.getSoundSystem().setVolume(id(), SoundHook.getVolumeLevel());
        }
    }

    @Override
    public void stop() {
        SoundHook.getSoundSystem().stop(id());
    }

    @Override
    public boolean isPlaying() {
        return SoundHook.getSoundSystem().playing(id());
    }

    protected String id() {
        return soundId.toString();
    }


}
