package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a "News Articles" object in collection users.
 */
public class ArticlesNotificationCategory {
    private Integer count;

    @DBRef
    private List<String> resources;

    @PersistenceConstructor
    public ArticlesNotificationCategory(Integer count, List<String> resources) {
        this.count = count;
        this.resources = resources;
    }

    public ArticlesNotificationCategory() {
        this.count = 0;
        this.resources = new ArrayList<>();
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