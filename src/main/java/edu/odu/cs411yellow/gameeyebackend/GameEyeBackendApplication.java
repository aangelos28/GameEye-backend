package edu.odu.cs411yellow.gameeyebackend;

import edu.odu.cs411yellow.gameeyebackend.config.Secrets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameEyeBackendApplication {

	public static void main(String[] args) {
		// Load secrets (such as API keys)
		Secrets.loadSecrets("secrets.json");

		SpringApplication.run(GameEyeBackendApplication.class, args);
	}
}
