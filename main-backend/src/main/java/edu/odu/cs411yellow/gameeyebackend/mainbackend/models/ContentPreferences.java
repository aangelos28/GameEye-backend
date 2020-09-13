package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.Content;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationPreferences;
import org.springframework.data.annotation.PersistenceConstructor;


public class ContentPreferences {
    private Content content;

    private NotificationPreferences notificationPreferences;

    @PersistenceConstructor
    public ContentPreferences(Content content, NotificationPreferences notificationPreferences) {
        this.content = content;
        this.notificationPreferences = notificationPreferences;
    }

    public Content getContent() {
        return this.content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public NotificationPreferences getNotifications() {
        return this.notificationPreferences;
    }

    public void setNotifications(NotificationPreferences notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

}
