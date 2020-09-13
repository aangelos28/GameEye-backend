package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

public class Content {
    private Boolean showArchivedResources;

    @PersistenceConstructor
    public Content(Boolean showArchivedResources) {
        this.showArchivedResources = showArchivedResources;
    }

    public Boolean getShowArchivedResources() {
        return this.showArchivedResources;
    }

    public void setShowArchivedResources(Boolean showArchivedResources) {
        this.showArchivedResources = showArchivedResources;
    }

}
