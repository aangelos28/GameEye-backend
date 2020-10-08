package edu.odu.cs411yellow.gameeyebackend.mainbackend.security;

import org.springframework.beans.factory.annotation.Value;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class IgdbTokenContainer {
    Logger logger = LoggerFactory.getLogger(IgdbTokenContainer.class);

    private String clientId;
    private String clientSecret;
    private String accessToken;
    private int expirationInSeconds;

    public IgdbTokenContainer(@Value("${igdb.clientid}") String clientId, @Value("${igdb.clientsecret}") String clientSecret) {
        retrieveAccessToken(clientId, clientSecret);

    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public void retrieveAccessToken(String clientId, String clientSecret) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://id.twitch.tv")
                .build();

        logger.info(String.format("Client id:", clientId));
        logger.info(String.format("Client secret:", clientSecret));

        AuthResponse authResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2/token")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("grant_type", "client_credentials")
                        .build())
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();

        logger.info(String.format("Client id:", authResponse.accessToken));

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessToken = authResponse.accessToken;
        this.expirationInSeconds = authResponse.expiresIn;
    }

}
