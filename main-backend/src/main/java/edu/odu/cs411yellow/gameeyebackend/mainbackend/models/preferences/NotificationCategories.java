package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.List;

public class NotificationCategories {

    private ArticlesNotificationCategory articles;

    @PersistenceConstructor
    public NotificationCategories(ArticlesNotificationCategory articles) {
        this.articles = articles;
    };

    public NotificationCategories() {
        this.articles = new ArticlesNotificationCategory();
    };

    public ArticlesNotificationCategory getArticles() {
        return articles;
    };

    public void setArticles(ArticlesNotificationCategory articles) {
        this.articles = articles;
    }

}
