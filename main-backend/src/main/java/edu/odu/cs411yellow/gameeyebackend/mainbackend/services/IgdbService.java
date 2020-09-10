package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for calling IGDB REST API.
 */
@Service
public class IgdbService {
    private final WebClient webClient;

    public IgdbService(WebClient.Builder webClientBuilder, @Value("${igdb.baseurl}") String igdbUrl, @Value("${apikeys.igdb}") String igdbKey) {
        this.webClient = webClientBuilder
                .baseUrl(igdbUrl)
                .defaultHeader("user-key", igdbKey)
                .build();
    }

    public String getCompanies() {
        return this.webClient.post()
                .uri("/companies")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("fields name; limit 10;")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
