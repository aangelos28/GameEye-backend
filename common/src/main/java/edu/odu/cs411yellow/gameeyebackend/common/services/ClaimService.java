package edu.odu.cs411yellow.gameeyebackend.common.services;

import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import edu.odu.cs411yellow.gameeyebackend.common.security.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for validating a user's claims contained in their
 * ID token.
 */
@Service
public class ClaimService {
    /**
     * Sets the passed list of roles as the roles for the user
     * identified by the passed ID token.
     * @param idToken Firebase ID token for the user.
     * @param roles List of roles to set for the user.
     * @return Future that completes once the async request is finished.
     */
    public ApiFuture<Void> setUserRolesAsync(FirebaseToken idToken, List<UserRole> roles) {
        String userId = idToken.getUid();

        Map<String, Object> userRoles = new HashMap<>();

        for (UserRole role : roles) {
            userRoles.put(role.name(), true);
        }

        // Set claims in Firebase token
        return FirebaseAuth.getInstance().setCustomUserClaimsAsync(userId, userRoles);
    }

    /**
     * Gets a user's custom claims. Makes an API call so this method should be used sparingly.
     * @param idToken Firebase ID token for the user.
     * @return Map containing the user's custom claims.
     * @throws FirebaseAuthException If failed to get custom token.
     */
    public Map<String, Object> getCustomClaims(FirebaseToken idToken) throws FirebaseAuthException {
        String userId = idToken.getUid();

        UserRecord user = FirebaseAuth.getInstance().getUser(userId);

        return user.getCustomClaims();
    }

    /**
     * Gets a user's roles as a List of Spring SimpleGrantedAuthority.
     * @param idToken Firebase ID token for the user.
     * @return List of Spring SimpleGrantedAuthority.
     */
    public List<SimpleGrantedAuthority> getUserRolesAsAuthorities(FirebaseToken idToken) {
        Set<String> roles = idToken.getClaims().keySet();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            if (role.equals(UserRole.admin.name())) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
        }

        return authorities;
    }

    /**
     * Checks if a user's email is verified.
     * @param idToken Firebase ID token for the user.
     * @return True if the user's email is verified, false otherwise.
     */
    public boolean userEmailVerified(FirebaseToken idToken) {
        Map<String, Object> userRoles = idToken.getClaims();

        Object emailVerified = userRoles.get("email_verified");

        if (emailVerified != null) {
            return (boolean) emailVerified;
        }

        // If there is no emailVerified claim then we are likely dealing
        // with an authorized CLI
        return (boolean) userRoles.get("authorizedCli");
    }
}
