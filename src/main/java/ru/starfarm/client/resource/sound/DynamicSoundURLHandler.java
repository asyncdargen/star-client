package ru.starfarm.client.resource.sound;

import lombok.RequiredArgsConstructor;
import ru.starfarm.client.api.resource.SoundResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

@RequiredArgsConstructor
public class DynamicSoundURLHandler extends URLStreamHandler {

    protected final SoundResource resource;


    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new URLConnection(url) {
            @Override
            public void connect() throws IOException {

            }

            @Override
            public InputStream getInputStream() throws IOException {
                return resource.openInputStream();
            }
        };
    }
}
