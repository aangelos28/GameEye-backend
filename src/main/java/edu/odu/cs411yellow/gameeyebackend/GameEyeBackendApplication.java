package edu.odu.cs411yellow.gameeyebackend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import edu.odu.cs411yellow.gameeyebackend.config.Secrets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class GameEyeBackendApplication {

	public static void main(String[] args) {
		// Load secrets (such as API keys)
		Secrets.loadSecrets("secrets.json");

		SpringApplication.run(GameEyeBackendApplication.class, args);

		// Initialize firebase
		try {
			InputStream firebaseCredentials = new ByteArrayInputStream(Secrets.getFirebaseCredentials().getBytes(StandardCharsets.UTF_8));
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(firebaseCredentials))
					.setDatabaseUrl("https://gameeye-8eb07.firebaseio.com")
					.build();
			FirebaseApp.initializeApp(options);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
