package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

public class NotificationCategories {

    private Articles articles;

    @PersistenceConstructor
    public NotificationCategories(Articles articles) {
        this.articles = articles;
    };

    public NotificationCategories() {
        this.articles = new Articles();
    };

    public Articles getArticles() {
        return articles;
    };

    public void setArticles(Articles articles) {
        this.articles = articles;
    }

}
