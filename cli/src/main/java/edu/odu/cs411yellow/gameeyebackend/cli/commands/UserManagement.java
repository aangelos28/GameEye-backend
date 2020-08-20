package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class UserManagement {
    @ShellMethod("Get a user's information.")
    public String getUser(@ShellOption() String userId) {
        return userId;
    }
}
