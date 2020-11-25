package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.notifications;

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
            this.articleIds.remove(id);
        }
    }

    public void removeAllArticles() {
        this.articleIds.clear();
    }
}
