package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

public class ContentPreferences {
    private Boolean showArchivedResources;

    @PersistenceConstructor
    public ContentPreferences(Boolean showArchivedResources) {
        this.showArchivedResources = showArchivedResources;
    }

    public Boolean getShowArchivedResources() {
        return this.showArchivedResources;
    }

    public void setShowArchivedResources(Boolean showArchivedResources) {
        this.showArchivedResources = showArchivedResources;
    }

}
