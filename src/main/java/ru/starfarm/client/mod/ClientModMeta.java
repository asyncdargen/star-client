package ru.starfarm.client.mod;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import ru.starfarm.client.mod.exception.InvalidModPropertiesException;

import java.io.InputStream;
import java.util.Properties;

@Data
public class ClientModMeta {

    protected final String name, version, main, author;

    public static ClientModMeta fromProperties(Properties properties) {
        val name = properties.getProperty("name");
        val version = properties.getProperty("version");
        val main = properties.getProperty("main");
        val author = properties.getProperty("author");

        if (name == null) throw new InvalidModPropertiesException("not specified name");
        if (main == null) throw new InvalidModPropertiesException("not specified main");

        return new ClientModMeta(name, version == null ? "beta" : version, main, author == null ? "StarFarm" : author);
    }

    @SneakyThrows
    public static ClientModMeta fromInputStream(InputStream inputStream) {
        if (inputStream == null) throw new InvalidModPropertiesException("not specified mod.properties file");
        val properties = new Properties();
        properties.load(inputStream);
        return fromProperties(properties);
    }

}
