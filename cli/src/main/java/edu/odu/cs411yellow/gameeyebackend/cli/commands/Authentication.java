package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import edu.odu.cs411yellow.gameeyebackend.cli.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

/**
 * Contains CLI commands related to authentication.
 */
@ShellComponent
public class Authentication {
    private final AuthenticationService authService;

    @Autowired
    Authentication(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Gets the Firebase user ID for this CLI application instance.
     *
     * @return User ID.
     */
    @ShellMethod("Gets the user ID for this CLI instance.")
    public String cliGetUid() {
        return this.authService.getUserId();
    }

    /**
     * Gets the Firebase ID token for this CLI application instance.
     *
     * @return ID token.
     */
    @ShellMethod("Gets the ID token for this CLI instance.")
    public String cliGetIdToken() {
        return this.authService.getIdToken();
    }
}
