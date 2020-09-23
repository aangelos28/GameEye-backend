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
public class Articles {
    private Integer count;

    private List<String> articleIds;

    @PersistenceConstructor
    public Articles(Integer count, List<String> articleIds) {
        this.count = count;
        this.articleIds = articleIds;
    }

    public Articles() {
        this.count = 0;
        this.articleIds = new ArrayList<>();
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<String> getArticleIds() {
        return this.articleIds;
    }

    public void setArticleIds(List<String> articleIds) {
        this.articleIds = articleIds;
    }

}