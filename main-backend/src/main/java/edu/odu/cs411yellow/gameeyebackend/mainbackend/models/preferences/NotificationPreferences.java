package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

public class NotificationPreferences {

    private boolean showArticleResources;
    private boolean showImageResources;
    private boolean notifyOnlyIfImportant;

    @PersistenceConstructor
    public NotificationPreferences(boolean showArticleResources, boolean showImageResources, boolean notifyOnlyIfImportant) {
        this.showArticleResources = showArticleResources;
        this.showImageResources = showImageResources;
        this.notifyOnlyIfImportant = notifyOnlyIfImportant;
    }

    public NotificationPreferences() {
        this.showArticleResources = true;
        this.showImageResources = true;
        this.notifyOnlyIfImportant = true;
    }

    public boolean getShowArticleResources() {
        return this.showArticleResources;
    }

    public void setShowArticleResources(boolean showArticleResources) {
        this.showArticleResources = showArticleResources;
    }

    public boolean getShowImageResources() {
        return this.showImageResources;
    }

    public void setShowImageCategories(boolean showImageResources) {
        this.showImageResources = showImageResources;
    }

    public boolean getNotifyOnlyIfImportant() {
        return notifyOnlyIfImportant;
    }

    public void setNotifyOnlyIfImportant(boolean notifyOnlyIfImportant) {
        this.notifyOnlyIfImportant = notifyOnlyIfImportant;
    }

}
