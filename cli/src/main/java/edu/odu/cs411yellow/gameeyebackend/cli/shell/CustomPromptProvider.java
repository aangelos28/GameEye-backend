package edu.odu.cs411yellow.gameeyebackend.cli.shell;

import edu.odu.cs411yellow.gameeyebackend.cli.security.AuthenticationService;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
public class CustomPromptProvider implements PromptProvider {

    private AuthenticationService authService;

    @Autowired
    public CustomPromptProvider(AuthenticationService authService) throws UnknownHostException {
        this.authService = authService;
    }

    @Override
    public AttributedString getPrompt() {
        return new AttributedString(this.authService.getUserId() + "> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }
}
