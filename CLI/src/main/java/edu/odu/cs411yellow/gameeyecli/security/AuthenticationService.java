package edu.odu.cs411yellow.gameeyecli.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthenticationService {
    private String idToken;

    private final String userId;
    private final String shortUserId;

    public AuthenticationService() {
        userId = "cli-" + UUID.randomUUID();
        shortUserId = this.userId.substring(0, this.userId.indexOf('-', this.userId.indexOf('-') + 1));
    }

    @PostConstruct
    private void init() throws FirebaseAuthException {
        this.acquireIdToken();
    }

    public void acquireIdToken() throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("admin", true);
        claims.put("authorizedCli", true);

        this.idToken = FirebaseAuth.getInstance().createCustomToken(userId, claims);
    }

    public String getShortUserId() {
        return this.shortUserId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getIdToken() {
        return this.idToken;
    }
}
