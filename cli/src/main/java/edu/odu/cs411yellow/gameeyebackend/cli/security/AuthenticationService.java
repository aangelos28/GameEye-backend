package edu.odu.cs411yellow.gameeyebackend.cli.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import edu.odu.cs411yellow.gameeyebackend.cli.model.IdTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private String idToken;

    private final String userId;

    private final WebClient webClient;

    private class AuthenticationRequest {
        public String token;
        public boolean returnSecureToken = true;
    }

    public AuthenticationService() throws UnknownHostException {
        userId = "cli-" + InetAddress.getLocalHost().getHostName();

        this.webClient = WebClient.builder()
                .baseUrl("https://identitytoolkit.googleapis.com")
                .build();
    }

    @PostConstruct
    private void init() throws FirebaseAuthException {
        this.acquireIdToken();
    }

    public void acquireIdToken() throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("admin", true);
        claims.put("authorizedCli", true);

        String customToken = FirebaseAuth.getInstance().createCustomToken(userId, claims);

        // Get ID token
        AuthenticationRequest authReq = new AuthenticationRequest();
        authReq.token = customToken;
        authReq.returnSecureToken = true;
        IdTokenResponse response = this.webClient.post()
                .uri("/v1/accounts:signInWithCustomToken?key=" + Secrets.Firebase.getWebApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authReq), AuthenticationRequest.class)
                .retrieve()
                .bodyToMono(IdTokenResponse.class)
                .block();

        this.idToken = response.idToken;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getIdToken() {
        return this.idToken;
    }
}
