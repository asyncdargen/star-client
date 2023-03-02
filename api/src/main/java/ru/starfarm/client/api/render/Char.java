package ru.starfarm.client.api.render;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.starfarm.client.api.resource.TextureResource;

@Getter
@RequiredArgsConstructor
public enum Char {

    STARFARM("https://client.starfarm.fun/resources/client/font/starfarm.png"),

    DISCORD("https://client.starfarm.fun/resources/client/font/discord.png"),
    VK("https://client.starfarm.fun/resources/client/font/vk.png"),
    TELEGRAM("https://client.starfarm.fun/resources/client/font/telegram.png"),

    COIN_COPPER("https://client.starfarm.fun/resources/client/font/coin_copper.png"),
    COIN_SILVER("https://client.starfarm.fun/resources/client/font/coin_silver.png"),
    COIN_GOLD("https://client.starfarm.fun/resources/client/font/coin_gold.png"),
    COIN_DIAMOND("https://client.starfarm.fun/resources/client/font/coin_diamond.png"),
    COIN_EMERALD("https://client.starfarm.fun/resources/client/font/coin_emerald.png"),
    ;

    private final String url;
    @Setter
    private String toString;
    @Setter
    private TextureResource texture;;

    @Override
    public String toString() {
        return toString;
    }
}
