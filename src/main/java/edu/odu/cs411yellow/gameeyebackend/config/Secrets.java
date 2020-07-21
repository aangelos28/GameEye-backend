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

    public static class Auth0 {
        private static String audience;
        private static String issuerUri;

        public static String getAudience() {
            return audience;
        }
        public static String getIssuerUri() {
            return issuerUri;
        }
    }

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

            // Read Auth0 secrets
            JsonNode auth0SecretsJson = secretsJson.get("auth0");
            Auth0.audience = auth0SecretsJson.get("audience").textValue();
            Auth0.issuerUri = auth0SecretsJson.get("issuer_uri").textValue();
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
