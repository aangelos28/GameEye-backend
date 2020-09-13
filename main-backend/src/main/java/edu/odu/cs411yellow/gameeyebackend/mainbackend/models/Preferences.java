package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.Content;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.Notifications;
import org.springframework.data.annotation.PersistenceConstructor;


public class Preferences {
    private Content content;

    private Notifications notifications;

    @PersistenceConstructor
    public Preferences(Content content, Notifications notifications) {
        this.content = content;
        this.notifications = notifications;
    }

    public Content getContent() {
        return this.content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Notifications getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

}
