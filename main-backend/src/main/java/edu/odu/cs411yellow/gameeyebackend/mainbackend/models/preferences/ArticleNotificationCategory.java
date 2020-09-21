package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * Class representing a "News Articles" object in collection users.
 */
public class ArticleNotificationCategory {
    private String type;

    private Integer count;

    @DBRef
    private List<Article> resources;

    @PersistenceConstructor
    public ArticleNotificationCategory(String type, Integer count, List<Article> resources) {
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