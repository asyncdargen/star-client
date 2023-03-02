package ru.starfarm.client.api.resource;

public interface SoundResource extends Resource {

    void play();

    void stop();

    boolean isPlaying();

}
