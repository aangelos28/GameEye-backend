package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

public class NotificationPreferences {

    private List<String> resourceCategories;

    @PersistenceConstructor
    public NotificationPreferences(List<String> resourceCategories) {
        this.resourceCategories = resourceCategories;
    }

    public NotificationPreferences() {
        this.resourceCategories = new ArrayList<>();
    }

    public List<String> getResourcesCategories() {
        return this.resourceCategories;
    }

    public void setResourceCategories(List<String> resourceCategories) {
        this.resourceCategories = resourceCategories;
    }

}