package edu.odu.cs411yellow.gameeyebackend.cli.model;

/**
 * Represents an ID token response from the Firebase services.
 */
public class IdTokenResponse {
    public String kind;
    public String idToken;
    public String refreshToken;
    public String expiresIn;
    public String isNewUser;

    public IdTokenResponse() {
    }

    public IdTokenResponse(String kind, String idToken, String refreshToken, String expiresIn, String isNewUser) {
        this.kind = kind;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.isNewUser = isNewUser;
    }
}
