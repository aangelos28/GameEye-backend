package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the articles object in the watchList in the users collection.
 */
public class ArticleNotifications {
    private List<String> articleIds;

    @PersistenceConstructor
    public ArticleNotifications(List<String> articleIds) {
        this.articleIds = articleIds;
    }

    public ArticleNotifications() {
        this.articleIds = new ArrayList<>();
    }

    public List<String> getArticleIds() {
        return this.articleIds;
    }

    public void setArticleIds(List<String> articleIds) {
        this.articleIds = articleIds;
    }

    public void removeArticles(List<String> articleIds) {
        for (String id: articleIds) {
            boolean result = this.articleIds.remove(id);
        }
    }

}
