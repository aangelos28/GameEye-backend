package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings;

import org.springframework.data.annotation.PersistenceConstructor;

public class NotificationSettings {
    private boolean receiveNotifications;
    private boolean receiveArticleNotifications;
    private boolean notifyOnlyIfImportant;

    @PersistenceConstructor
    public NotificationSettings(boolean receiveNotifications, boolean receiveArticleNotifications, boolean notifyOnlyIfImportant) {
        this.receiveNotifications = receiveNotifications;
        this.receiveArticleNotifications = receiveArticleNotifications;
        this.notifyOnlyIfImportant = notifyOnlyIfImportant;
    }

    public NotificationSettings() {
        this.receiveNotifications = true;
        this.receiveArticleNotifications = true;
        this.notifyOnlyIfImportant = true;
    }

    public boolean getReceiveNotifications() {
        return receiveNotifications;
    }

    public void setReceiveNotifications(boolean receiveNotifications) {
        this.receiveNotifications = receiveNotifications;
    }

    public boolean getReceiveArticleNotifications() {
        return this.receiveArticleNotifications;
    }

    public void setReceiveArticleNotifications(boolean receiveArticleNotifications) {
        this.receiveArticleNotifications = receiveArticleNotifications;
    }

    public boolean getNotifyOnlyIfImportant() {
        return notifyOnlyIfImportant;
    }

    public void setNotifyOnlyIfImportant(boolean notifyOnlyIfImportant) {
        this.notifyOnlyIfImportant = notifyOnlyIfImportant;
    }

}
