package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import edu.odu.cs411yellow.gameeyebackend.cli.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class Authentication {
    private final AuthenticationService authService;

    @Autowired
    Authentication(AuthenticationService authService) {
        this.authService = authService;
    }

    @ShellMethod("Gets the user ID for this CLI instance.")
    public String cliGetUid() {
        return this.authService.getUserId();
    }

    @ShellMethod("Gets the ID token for this CLI instance.")
    public String cliGetIdToken() {
        return this.authService.getIdToken();
    }
}
