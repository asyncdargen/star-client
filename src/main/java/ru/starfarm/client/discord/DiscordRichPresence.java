package ru.starfarm.client.discord;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//@Getter @Setter
//@EqualsAndHashCode(callSuper = false)
public class DiscordRichPresence extends Structure {

    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
            "state", "details", "startTimestamp", "endTimestamp", "largeImageKey",
            "largeImageText", "smallImageKey", "smallImageText", "partyId", "partySize",
            "partyMax", "matchSecret", "joinSecret", "spectateSecret", "instance"
    ));

    public DiscordRichPresence(String encoding) {
        super();
        setStringEncoding(encoding);
    }

    public DiscordRichPresence() {
        this("UTF-8");
    }

    public String state;
    public String details;
    public long startTimestamp;
    public long endTimestamp;
    public String largeImageKey;
    public String largeImageText;
    public String smallImageKey;
    public String smallImageText;
    public String partyId;
    public int partySize;
    public int partyMax;
    public String matchSecret;
    public String joinSecret;
    public String spectateSecret;
    public int instance;

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }

}
