package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

public class NotificationPreferences {

    private boolean showArticleResources;
    private boolean showImageResources;

    @PersistenceConstructor
    public NotificationPreferences(boolean showArticleResources, boolean showImageResources) {
        this.showArticleResources = showArticleResources;
        this.showImageResources = showImageResources;
    }

    public NotificationPreferences() {
        this.showArticleResources = true;
        this.showImageResources = true;
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

}