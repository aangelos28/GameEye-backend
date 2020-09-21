package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.List;

/**
 * Class representing a document in the "notificationCategories" array collection.
 */
public class NotificationCategory {
    private String type;

    private Integer count;

    private List<String> resources;

    @PersistenceConstructor
    public NotificationCategory(String type, Integer count, List<String> resources) {
        this.type = type;
        this.count = count;
        this.resources = resources;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getResources() {
        return this.resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

}