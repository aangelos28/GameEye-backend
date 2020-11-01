package edu.odu.cs411yellow.gameeyebackend.cli;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import edu.odu.cs411yellow.gameeyebackend.common.security.FirebaseSecrets;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * GameEye CLI Spring application class.
 */
@SpringBootApplication()
@ComponentScan(value = {"edu.odu.cs411yellow.gameeyebackend.cli", "edu.odu.cs411yellow.gameeyebackend.common"})
public class CliApplication {
    /**
     * CLI entry point.
     *
     * @param args Array of CLI arguments.
     */
    public static void main(String[] args) {
        // Load secrets (such as API keys)
        FirebaseSecrets.read("firebase.json");

        // Initialize firebase
        try {
            InputStream firebaseCredentials = new ByteArrayInputStream(FirebaseSecrets.getCredentials().getBytes(StandardCharsets.UTF_8));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(firebaseCredentials))
                    .setDatabaseUrl("https://gameeye-8eb07.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConfigurableApplicationContext context = new SpringApplicationBuilder(CliApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
