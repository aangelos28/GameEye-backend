package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import edu.odu.cs411yellow.gameeyebackend.cli.security.AuthenticationService;
import edu.odu.cs411yellow.gameeyebackend.common.security.UserRole;
import edu.odu.cs411yellow.gameeyebackend.common.services.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Map;

@ShellComponent
public class UserManagement {

    private final AuthenticationService authService;
    private final ClaimService claimService;

    @Autowired
    public UserManagement(AuthenticationService authService, ClaimService claimService) {
        this.authService = authService;
        this.claimService = claimService;
    }

    @ShellMethod("Get a user's information.")
    public void getUser(@ShellOption() String id) throws FirebaseAuthException {
        UserRecord user = claimService.getUserRecord(id);

        System.out.printf("  Uid: %s\n", user.getUid());
        System.out.printf("  Email: %s\n", user.getEmail());

        Map<String, Object> roles = user.getCustomClaims();
        System.out.println("  Roles:");
        for (String role : roles.keySet()) {
            System.out.printf("   - %s\n", role);
        }
    }

    @ShellMethod("Get a user's information.")
    public void searchUser(@ShellOption() String email) throws FirebaseAuthException {
        UserRecord user = claimService.getUserRecordByEmail(email);

        System.out.printf("  Uid: %s\n", user.getUid());
        System.out.printf("  Email: %s\n", user.getEmail());

        Map<String, Object> roles = user.getCustomClaims();
        System.out.println("  Roles:");
        for (String role : roles.keySet()) {
            System.out.printf("   - %s\n", role);
        }
    }

    @ShellMethod("Add role to user")
    public String addUserRole(@ShellOption() String id, @ShellOption() String role) throws Exception {
        ApiFuture<Void> future = claimService.addUserRoleAsync(id, UserRole.valueOf(role));
        future.get();

        return String.format("Role %s added to user %s.%n", role, id);
    }

    @ShellMethod("Remove role from user")
    public String removeUserRole(@ShellOption() String id, @ShellOption() String role) throws Exception {
        ApiFuture<Void> future = claimService.removeUserRoleAsync(id, UserRole.valueOf(role));
        future.get();

        return String.format("Role %s removed from user %s.%n", role, id);
    }
}
