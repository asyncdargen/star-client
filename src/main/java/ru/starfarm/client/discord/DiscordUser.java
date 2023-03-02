package ru.starfarm.client.discord;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscordUser extends Structure {

    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
            "userId", "username", "discriminator", "avatar"
    ));

    public DiscordUser(String encoding) {
        super();
        setStringEncoding(encoding);
    }

    public DiscordUser() {
        this("UTF-8");
    }

    public String userId;
    public String username;
    public String discriminator;
    public String avatar;

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }

}
