package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ContentPreferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationPreferences;
import org.springframework.data.annotation.PersistenceConstructor;


public class Preferences {
    private ContentPreferences contentPreferences;

    private NotificationPreferences notificationPreferences;

    @PersistenceConstructor
    public Preferences(ContentPreferences contentPreferences, NotificationPreferences notificationPreferences) {
        this.contentPreferences = contentPreferences;
        this.notificationPreferences = notificationPreferences;
    }

    public ContentPreferences getContent() {
        return this.contentPreferences;
    }

    public void setContent(ContentPreferences contentPreferences) {
        this.contentPreferences = contentPreferences;
    }

    public NotificationPreferences getNotifications() {
        return this.notificationPreferences;
    }

    public void setNotifications(NotificationPreferences notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

}
