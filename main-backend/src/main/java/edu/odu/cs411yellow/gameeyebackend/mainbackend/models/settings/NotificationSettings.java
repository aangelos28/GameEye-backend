package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings;

import org.springframework.data.annotation.PersistenceConstructor;

public class NotificationSettings {
    private boolean receiveNotifications;
    private boolean showArticleResources;
    private boolean notifyOnlyIfImportant;

    @PersistenceConstructor
    public NotificationSettings(boolean receiveNotifications, boolean showArticleResources, boolean notifyOnlyIfImportant) {
        this.receiveNotifications = receiveNotifications;
        this.showArticleResources = showArticleResources;
        this.notifyOnlyIfImportant = notifyOnlyIfImportant;
    }

    public NotificationSettings() {
        this.receiveNotifications = true;
        this.showArticleResources = true;
        this.notifyOnlyIfImportant = true;
    }

    public boolean isReceiveNotifications() {
        return receiveNotifications;
    }

    public void setReceiveNotifications(boolean receiveNotifications) {
        this.receiveNotifications = receiveNotifications;
    }

    public boolean getShowArticleResources() {
        return this.showArticleResources;
    }

    public void setShowArticleResources(boolean showArticleResources) {
        this.showArticleResources = showArticleResources;
    }

    public boolean getNotifyOnlyIfImportant() {
        return notifyOnlyIfImportant;
    }

    public void setNotifyOnlyIfImportant(boolean notifyOnlyIfImportant) {
        this.notifyOnlyIfImportant = notifyOnlyIfImportant;
    }

}
