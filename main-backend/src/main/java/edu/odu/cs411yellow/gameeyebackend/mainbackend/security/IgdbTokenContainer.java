package edu.odu.cs411yellow.gameeyebackend.mainbackend.security;

import com.google.api.client.util.Value;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class IgdbTokenContainer implements InitializingBean {
    private String accessToken;
    @Value("${igdb.clientid}")
    private String clientId;
    @Value("${igdb.clientsecret}")
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
        WebClient webClient = WebClient.builder()
                .baseUrl("https://id.twitch.tv")
                .build();

        System.out.println(webClient.toString());

        IgdbModel.AuthResponse authResponse = webClient.post()
                .uri("/oauth2/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("?client_id=" + this.clientId +
                           "&client_secret=" + this.clientSecret +
                           "&grant_type=client_credentials")
                .retrieve()
                .bodyToMono(IgdbModel.AuthResponse.class)
                .block();
        this.accessToken = authResponse.access_token;
        this.expirationInSeconds = authResponse.expires_in;

    }
}
