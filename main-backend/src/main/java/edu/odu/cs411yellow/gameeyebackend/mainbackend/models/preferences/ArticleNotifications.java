package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the articles object in the watchList in the users collection.
 */
public class ArticleNotifications {
    private Integer count;

    private List<String> articleIds;

    @PersistenceConstructor
    public ArticleNotifications(Integer count, List<String> articleIds) {
        this.count = count;
        this.articleIds = articleIds;
    }

    public ArticleNotifications() {
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
