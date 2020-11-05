package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings.NotificationSettings;
import org.springframework.data.annotation.PersistenceConstructor;


public class Settings {
    private NotificationSettings notificationSettings;

    @PersistenceConstructor
    public Settings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

    public Settings() {
        this.notificationSettings = new NotificationSettings();
    }

    public NotificationSettings getNotificationSettings() {
        return this.notificationSettings;
    }

    public void setNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

}

