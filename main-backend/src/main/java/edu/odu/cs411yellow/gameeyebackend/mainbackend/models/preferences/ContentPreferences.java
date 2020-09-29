package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

public class ContentPreferences {
    private boolean showArchivedResources;
    private boolean showImpactScores;

    @PersistenceConstructor
    public ContentPreferences(Boolean showArchivedResources, boolean showImpactScores) {
        this.showArchivedResources = showArchivedResources;
        this.showImpactScores = showImpactScores;
    }

    // Default constructor
    public ContentPreferences() {
        this.showArchivedResources = false;
        this.showImpactScores = false;
    }

    public boolean getShowArchivedResources() {
        return this.showArchivedResources;
    }

    public void setShowArchivedResources(boolean showArchivedResources) {
        this.showArchivedResources = showArchivedResources;
    }

    public boolean getShowImpactScores() {
        return this.showImpactScores;
    }

    public void setShowImpactScores(boolean showImpactScores) {
        this.showImpactScores = showImpactScores;
    }

}
