package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.List;

public class Notifications {
    private String notificationFrequency;

    private List<String> resourceCategories;

    @PersistenceConstructor
    public Notifications(String notificationFrequency, List<String> resourceCategories) {
        this.notificationFrequency = notificationFrequency;
        this.resourceCategories = resourceCategories;
    }

    public String getNotificationFrequency() {
        return this.notificationFrequency;
    }

    public void setNotificationFrequency(String notificationFrequency) {
        this.notificationFrequency = notificationFrequency;
    }

    public List<String> getResourcesCategories() {
        return this.resourceCategories;
    }

    public void setResourceCategories(List<String> resourceCategories) {
        this.resourceCategories = resourceCategories;
    }

}