package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentPreferences {
    private boolean showArchivedResources;
    private boolean showImpactScores;

    /**
     * Each true/false value indicates whether resources with the corresponding
     * impact scores will be visible for a user.
     * impactScores[0] -> show/hide resources with impact scores == 1.
     * impactScores[1] -> show/hide resources with impact scores == 2.
     * impactScores[2] -> show/hide resources with impact scores == 3.
     */
    private List<Boolean> impactScores;

    @PersistenceConstructor
    public ContentPreferences(boolean showArchivedResources, boolean showImpactScores,
                              List<Boolean> impactScores) {
        this.showArchivedResources = showArchivedResources;
        this.showImpactScores = showImpactScores;
        this.impactScores = impactScores;

    }

    // Default constructor
    public ContentPreferences() {
        this.showArchivedResources = false;
        this.showImpactScores = true;
        this.impactScores = new ArrayList<>(Arrays.asList(true, true, true));

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

    public List<Boolean> getImpactScores() {
        return this.impactScores;
    }

    public void setImpactScores(List<Boolean> impactScores) {
        this.impactScores = impactScores;
    }

}
