package edu.odu.cs411yellow.gameeyebackend.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Class that contains application secrets.
 */
public class Secrets {
    /**
     * IGDB API key.
     */
    private static String igdbKey;

    /**
     * Load secrets from a JSON resource file. The file must be
     * in the classpath (for example under resources/). This function must be called
     * once in the beginning of the application so that other code can utilize its
     * values.
     * @param secretsResourcePath Path to the secrets JSON file under the classpath
     */
    public static void loadSecrets(String secretsResourcePath) {
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource secretsFile = resourceLoader.getResource(String.format("classpath:%s", secretsResourcePath));
            Reader jsonReader = new InputStreamReader(secretsFile.getInputStream(), UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode secretsJson = mapper.readTree(jsonReader);

            // Read IGDB key
            igdbKey = secretsJson.get("igdb_api_key").textValue();
        }
        catch (IOException e) {
            System.err.println("Fatal Error: Could not find secrets.json.");
            e.printStackTrace();
        }
    }

    public static String getIgdbKey() {
        return igdbKey;
    }
}
