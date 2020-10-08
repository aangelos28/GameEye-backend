package edu.odu.cs411yellow.gameeyebackend.mainbackend.security;

import com.google.api.client.util.Value;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class IgdbTokenContainer implements InitializingBean {
    private String accessToken;
    @Value("{igdb.clientid}")
    private String clientId;
    @Value("{igdb.clientsecret}")
    private String clientSecret;
    private int expirationInSeconds;

    public IgdbTokenContainer() {
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

    @Override
    public void afterPropertiesSet() throws Exception {
        //String clientId="";
        //String clientSecret = "";
        WebClient webClient = WebClient.builder()
                .baseUrl("https://id.twitch.tv")
                .build();

        System.out.println(webClient.toString());

        IgdbModel.AuthResponse authResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                .path("/oauth2/token")
                .queryParam("client_id", this.clientId)
                .queryParam("client_secret", this.clientSecret)
                .queryParam("grant_type", "client_credentials")
                .build())
                .retrieve()
                .bodyToMono(IgdbModel.AuthResponse.class)
                .block();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessToken = authResponse.accessToken;
        this.expirationInSeconds = authResponse.expiresIn;

    }
}
