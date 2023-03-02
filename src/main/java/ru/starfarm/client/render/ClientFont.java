package ru.starfarm.client.render;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.val;
import ru.starfarm.client.StarClient;
import ru.starfarm.client.api.render.Char;
import ru.starfarm.client.api.resource.TextureResource;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ClientFont {

    private int START_INDEX = 13312 + 3500, END_INDEX = 13312 + 3500;
    private final Pattern EMOJI_PATTERN = Pattern.compile("(?:[\\u2700-\\u27bf]|[\\ud83c\\udde6-\\ud83c\\uddff]{2}|[\\ud800\\udc00-\\uDBFF\\uDFFF]|[\\u2600-\\u26FF])[\\ufe0e\\ufe0f]?(?:[\\u0300-\\u036f\\ufe20-\\ufe23\\u20d0-\\u20f0]|[\\ud83c\\udffb-\\ud83c\\udfff])?(?:\\u200d(?:[^\\ud800-\\udfff]|(?:[\\ud83c\\udde6-\\ud83c\\uddff]){2}|[\\ud800\\udc00-\\uDBFF\\uDFFF]|[\\u2600-\\u26FF])[\\ufe0e\\ufe0f]?(?:[\\u0300-\\u036f\\ufe20-\\ufe23\\u20d0-\\u20f0]|[\\ud83c\\udffb-\\ud83c\\udfff])?)*|[\\u0023-\\u0039]\\ufe0f?\\u20e3|\\u3299|\\u3297|\\u303d|\\u3030|\\u24c2|[\\ud83c\\udd70-\\ud83c\\udd71]|[\\ud83c\\udd7e-\\ud83c\\udd7f]|\\ud83c\\udd8e|[\\ud83c\\udd91-\\ud83c\\udd9a]|[\\ud83c\\udde6-\\ud83c\\uddff]|[\\ud83c\\ude01-\\ud83c\\ude02]|\\ud83c\\ude1a|\\ud83c\\ude2f|[\\ud83c\\ude32-\\ud83c\\ude3a]|[\\ud83c\\ude50-\\ud83c\\ude51]|\\u203c|\\u2049|[\\u25aa-\\u25ab]|\\u25b6|\\u25c0|[\\u25fb-\\u25fe]|\\u00a9|\\u00ae|\\u2122|\\u2139|\\ud83c\\udc04|[\\u2600-\\u26FF]|\\u2b05|\\u2b06|\\u2b07|\\u2b1b|\\u2b1c|\\u2b50|\\u2b55|\\u231a|\\u231b|\\u2328|\\u23cf|[\\u23e9-\\u23f3]|[\\u23f8-\\u23fa]|\\ud83c\\udccf|\\u2934|\\u2935|[\\u2190-\\u21ff]", 256);

    private final Map<String, Integer> EMOJI_KEYS = new HashMap<>();
    private final Map<Integer, TextureResource> EMOJI_TEXTURES = new HashMap<>();

    public static void load() {
        //loadStandardEmoji();
        loadClientEmoji();
    }

    private void loadStandardEmoji() {
        val map = StarClient.instance().resourceHandler().binary("https://client.starfarm.fun/resources/client/font/emojis.json");
        map.joinLoad();

        EMOJI_KEYS.putAll(new Gson().<Map<String, Integer>>fromJson(map.getString(), new HashMapType()));
        END_INDEX += EMOJI_KEYS.size();
    }

    private void loadClientEmoji() {
        for (Char value : Char.values()) {
            val ch = ++END_INDEX;
            val texture = StarClient.instance().resourceHandler().texture(value.getUrl());
            value.setToString(String.valueOf((char) ch));
            value.setTexture(texture);
            EMOJI_TEXTURES.put(ch - START_INDEX, texture);
        }
    }

    public boolean isClientChar(char ch) {
        return ch > START_INDEX && ch <=  END_INDEX;
    }

    public TextureResource getTexture(char ch) {
        return EMOJI_TEXTURES.computeIfAbsent(((int) ch) - START_INDEX, code ->
                StarClient.instance().resourceHandler().texture("https://client.starfarm.fun/resources/client/font/e_" + code + ".png"));
    }

    public String resolveEmojis(String text) {
        return replaceByPattern(text, matcher -> {
            val builder = new StringBuilder();
            val group = matcher.group();
            for (int i = 0; i < group.length(); i++) {
                if (i > 0) builder.append("-");
                val n = group.codePointAt(i);
                builder.append(Integer.toHexString(n));
                if (n > 65535) i++;
            }
            val key = EMOJI_KEYS.get(builder.toString());
            return key == null ? group : String.valueOf((char) (START_INDEX + key));
        });
    }

    private String replaceByPattern(String text, Function<Matcher, String> mapper) {
        val builder = new StringBuffer();
        val matcher = EMOJI_PATTERN.matcher(text);
        while (matcher.find())
            matcher.appendReplacement(builder, mapper.apply(matcher));
        matcher.appendTail(builder);
        return builder.toString();
    }

    static class HashMapType implements ParameterizedType {

        @Override
        public Type[] getActualTypeArguments() {
            return new Class[]{String.class, Integer.class};
        }

        @Override
        public Type getRawType() {
            return HashMap.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
