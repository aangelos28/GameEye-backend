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
     * identified by the passed ID.
     * @param userId Firebase user ID.
     * @param roles List of roles to set for the user.
     * @return Future that completes once the async request is finished.
     */
    public ApiFuture<Void> setUserRolesAsync(String userId, List<UserRole> roles) {
        Map<String, Object> userRoles = new HashMap<>();

        for (UserRole role : roles) {
            userRoles.put(role.name(), true);
        }

        // Set claims in Firebase token
        return FirebaseAuth.getInstance().setCustomUserClaimsAsync(userId, userRoles);
    }

    /**
     * Adds the passed role as a new role for the user identified by the passed ID.
     * The role will not be added if it already exists.
     * @param userId Firebase user ID.
     * @param role Role to add for the user.
     * @return Future that completes once the async request is finished.
     * @throws Exception Thrown if roles has already been added for the user.
     */
    public ApiFuture<Void> addUserRoleAsync(String userId, UserRole role) throws Exception {
        Map<String, Object> userRoles = getCustomClaims(userId);

        String roleString = role.name();
        for (String userRole : userRoles.keySet()) {
            if (userRole.equals(roleString)) {
                throw new Exception(String.format("User role %s already exists for user %s", userRole, userId));
            }
        }

        Map<String, Object> newUserRoles = new HashMap<String, Object>(userRoles);
        newUserRoles.put(roleString, true);

        // Set claims in Firebase token
        return FirebaseAuth.getInstance().setCustomUserClaimsAsync(userId, newUserRoles);
    }

    /**
     * Removes the passed role for the user identified by the passed ID.
     * @param userId Firebase user ID.
     * @param role Role to remove from the user.
     * @return Future that completes once the async request is finished.
     * @throws Exception Thrown if role does not exist for user.
     */
    public ApiFuture<Void> removeUserRoleAsync(String userId, UserRole role) throws Exception {
        Map<String, Object> userRoles = getCustomClaims(userId);

        String roleString = role.name();
        for (String userRole : userRoles.keySet()) {
            if (userRole.equals(roleString)) {
                Map<String, Object> newUserRoles = new HashMap<String, Object>(userRoles);
                newUserRoles.remove(roleString);

                // Set claims in Firebase token
                return FirebaseAuth.getInstance().setCustomUserClaimsAsync(userId, newUserRoles);
            }
        }

        throw new Exception(String.format("User role %s does not exist for user %s", roleString, userId));
    }

    /**
     * Gets the record for the user identified by the passed ID.
     * @param userId ID of the user.
     * @return User record for the user.
     * @throws FirebaseAuthException Thrown if failed to get the record for the requested user.
     */
    public UserRecord getUserRecord(String userId) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUser(userId);
    }

    /**
     * Gets a user's custom claims. Makes an API call so this method should be used sparingly.
     * @param userId Firebase user ID.
     * @return Map containing the user's custom claims.
     * @throws FirebaseAuthException If failed to get custom token.
     */
    public Map<String, Object> getCustomClaims(String userId) throws FirebaseAuthException {
        UserRecord user = getUserRecord(userId);

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
