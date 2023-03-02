package ru.starfarm.client.util;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.w3c.dom.Element;
import ru.starfarm.client.StarClient;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

@UtilityClass
public class Updater {

    private final boolean ENABLED_UPDATER = Boolean.parseBoolean(System.getProperty("client.updater", "true"));

    @Data
    class VersionResult {

        private final String version;
        private final boolean outdated;

    }

    @SneakyThrows
    private InputStream request(String path) {
        val connection = ((HttpURLConnection) new URL("https://repo.starfarm.fun/public/ru/starfarm/star-client/" + path).openConnection());
        connection.setDoInput(true);
        connection.addRequestProperty("User-Agent", "StarClient/1.0");
        return connection.getInputStream();
    }

    @SneakyThrows
    private VersionResult requestVersionResult() {
        if (!ENABLED_UPDATER) return new VersionResult("", false);

        val metadata = ((Element) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(request("maven-metadata.xml")).getChildNodes().item(0));
        val versioning = ((Element) metadata.getElementsByTagName("versioning").item(0));
        val latestVersion = versioning.getElementsByTagName("latest").item(0).getTextContent();

        StarClient.LOGGER.info("Latest version: " + latestVersion);

        return new VersionResult(latestVersion, !latestVersion.equals(StarClient.version()));
    }

    public boolean checkAndUpdate() {
        val result = requestVersionResult();
        if (result.isOutdated()) {
            try {
                val latestMod = request(String.format("%s/star-client-%1$1s.jar", result.getVersion()));
                try (val outputStream = Files.newOutputStream(StarClient.modContainer().getSource().toPath())) {
                    byte[] buffer = new byte[1024 * 4];
                    int length;
                    while ((length = latestMod.read(buffer)) != -1)
                        outputStream.write(buffer, 0, length);
                    outputStream.flush();
                } catch (Throwable throwable) {
                    StarClient.LOGGER.error("Error while saving client-mod", throwable);
                    return false;
                }
            } catch (Throwable throwable) {
                StarClient.LOGGER.error("Error while updating client-mod", throwable);
                return false;
            }
            return true;
        }
        return false;
    }

}
