package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

public class Notifications {
    @Id
    private final String id;

    private String notificationFrequency;

    private List<String> resourceCategories;

    @PersistenceConstructor
    public Notifications(String id, String notificationFrequency, List<String> resourceCategories) {
        this.id = id;
        this.notificationFrequency = notificationFrequency;
        this.resourceCategories = resourceCategories;
    }

    public String getId() {
        return this.id;
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