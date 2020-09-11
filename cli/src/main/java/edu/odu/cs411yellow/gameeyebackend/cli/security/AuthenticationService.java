package edu.odu.cs411yellow.gameeyebackend.cli.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.cli.model.IdTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for authenticating GameEye CLI with Firebase.
 */
@Service
public class AuthenticationService {
    /**
     * String representation of ID token retrieved from Firebase.
     */
    private String idToken;

    /**
     * ID token retrieved from Firebase.
     */
    private FirebaseToken firebaseIdToken;

    /**
     * User ID of CLI instance.
     */
    private final String userId;

    private final WebClient webClient;

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    /**
     * Firebase web API key.
     */
    @Value("${apikeys.firebaseWeb}")
    private String firebaseWebApiKey;

    /**
     * Data for authentication request to Firebase web API.
     */
    private static class AuthenticationRequest {
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

    /**
     * Acquired ID token for this CLI instance from Firebase.
     *
     * @throws FirebaseAuthException Thrown if failed to acquire ID token.
     */
    public void acquireIdToken() throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("admin", true);
        claims.put("authorizedCli", true);

        String customToken = FirebaseAuth.getInstance().createCustomToken(userId, claims);

        // Get ID token
        logger.info("Getting ID token");
        AuthenticationRequest authReq = new AuthenticationRequest();
        authReq.token = customToken;
        authReq.returnSecureToken = true;
        IdTokenResponse response = this.webClient.post()
                .uri("/v1/accounts:signInWithCustomToken?key=" + firebaseWebApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authReq), AuthenticationRequest.class)
                .retrieve()
                .bodyToMono(IdTokenResponse.class)
                .block();

        assert response != null;
        firebaseIdToken = FirebaseAuth.getInstance().verifyIdToken(response.idToken);

        logger.info("Retrieved ID token");
        this.idToken = response.idToken;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getIdToken() {
        return this.idToken;
    }

    public FirebaseToken getFirebaseIdToken() {
        return this.firebaseIdToken;
    }
}
