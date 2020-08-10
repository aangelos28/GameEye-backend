package edu.odu.cs411yellow.gameeyecli.shell;

import edu.odu.cs411yellow.gameeyecli.security.AuthenticationService;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider {

    private AuthenticationService authService;

    @Autowired
    public CustomPromptProvider(AuthenticationService authService) {
        this.authService = authService;
    }

    @Override
    public AttributedString getPrompt() {
        return new AttributedString(this.authService.getShortUserId() + "> ", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }
}
