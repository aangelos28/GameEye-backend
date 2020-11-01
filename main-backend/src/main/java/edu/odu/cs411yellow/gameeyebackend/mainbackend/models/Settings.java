package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationPreferences;
import org.springframework.data.annotation.PersistenceConstructor;


public class Settings {
    private NotificationPreferences notificationPreferences;

    @PersistenceConstructor
    public Settings(NotificationPreferences notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

    public Settings() {
        this.notificationPreferences = new NotificationPreferences();
    }

    public NotificationPreferences getNotifications() {
        return this.notificationPreferences;
    }

    public void setNotifications(NotificationPreferences notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

}

