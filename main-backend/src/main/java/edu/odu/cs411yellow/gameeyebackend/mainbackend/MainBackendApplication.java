package edu.odu.cs411yellow.gameeyebackend.mainbackend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import edu.odu.cs411yellow.gameeyebackend.common.security.FirebaseSecrets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Main backend Spring application class.
 */
@SpringBootApplication
@ComponentScan({"edu.odu.cs411yellow.gameeyebackend.mainbackend", "edu.odu.cs411yellow.gameeyebackend.common"})
public class MainBackendApplication {

	/**
	 * Main backend entry point.
	 * @param args Array of CLI arguments.
	 */
	public static void main(String[] args) {
		// Read Firebase secrets
		FirebaseSecrets.read("firebase.json");

		// Initialize firebase
		try {
			InputStream firebaseCredentials = new ByteArrayInputStream(FirebaseSecrets.getCredentials().getBytes(StandardCharsets.UTF_8));
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(firebaseCredentials))
					.setDatabaseUrl("https://gameeye-8eb07.firebaseio.com")
					.build();
			FirebaseApp.initializeApp(options);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		SpringApplication.run(MainBackendApplication.class, args);
	}
}
