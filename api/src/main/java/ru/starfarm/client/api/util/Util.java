package ru.starfarm.client.api.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@UtilityClass
public class Util {

    private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[\\dA-FK-ORX]");

    public String stripColor(String input) {
        if (input == null)
            return null;

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public int rgb(int r, int g, int b) {
        return rgba(r, g, b, 255);
    }

    public int rgba(int r, int g, int b, int a) {
        return (0xFF & a) << 24 | (0xFF & r) << 16 | (0xFF & g) << 8 | 0xFF & b;
    }

    @SneakyThrows
    public URI uri(String uri) {
        return new URI(uri);
    }

    @SuppressWarnings("ALL")
    public <K, V> Map<K, V> mapOf(Object... elements) {
        val map = new HashMap<K, V>();

        K key = null;
        for (Object element : elements) {
            if (key == null) key = (K) element;
            else {
                map.put(key, (V) element);
                key = null;
            }
        }

        return map;
    }

}
