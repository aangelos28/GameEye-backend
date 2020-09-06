package edu.odu.cs411yellow.gameeyebackend.cli.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Class that contains Firebase secrets.
 */
public class FirebaseSecrets {
    private static String audience;
    private static String issuerUri;
    private static String credentials;

    /**
     * Read Firebase service account credentials from a JSON file.
     * The file must be in the classpath (for example under resources/). This function must be called
     * once in the beginning of the application so that other code can utilize its
     * values.
     *
     * @param firebaseSecretsFileName Path to the JSON file under the classpath
     */
    public static void read(String firebaseSecretsFileName) {
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource secretsFile = resourceLoader.getResource(String.format("classpath:%s", firebaseSecretsFileName));
            Reader jsonReader = new InputStreamReader(secretsFile.getInputStream(), UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode secretsJson = mapper.readTree(jsonReader);

            // Read Firebase secrets
            JsonNode firebaseSecretsJson = secretsJson.get("firebase");
            audience = firebaseSecretsJson.get("audience").textValue();
            issuerUri = firebaseSecretsJson.get("issuer_uri").textValue();
            credentials = firebaseSecretsJson.get("credentials").toString();
        } catch (IOException e) {
            System.err.println("Fatal Error: Could not find firebase.json.");
            e.printStackTrace();
        }
    }

    public static String getAudience() {
        return audience;
    }

    public static String getIssuerUri() {
        return issuerUri;
    }

    public static String getCredentials() {
        return credentials;
    }

}
