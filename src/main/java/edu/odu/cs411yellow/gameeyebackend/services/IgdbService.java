package edu.odu.cs411yellow.gameeyebackend.services;

import edu.odu.cs411yellow.gameeyebackend.config.Secrets;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class IgdbService {
    private final WebClient webClient;

    private final String BASE_URL = "https://api-v3.igdb.com";

    private final String API_KEY = Secrets.getIgdbKey();

    public IgdbService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(BASE_URL)
                .defaultHeader("user-key", API_KEY)
                .build();
    }

    public String getCompanies() {
        String output = this.webClient.post()
                .uri("/companies")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("fields name; limit 10;")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return output;
    }
}
