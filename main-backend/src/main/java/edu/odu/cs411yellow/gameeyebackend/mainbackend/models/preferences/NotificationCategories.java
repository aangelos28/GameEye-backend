package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

public class NotificationCategories {

    private ArticleNotifications articleNotifications;

    private ImageNotifications imageNotifications;

    @PersistenceConstructor
    public NotificationCategories(ArticleNotifications articleNotifications, ImageNotifications imageNotifications) {
        this.articleNotifications = articleNotifications;
        this.imageNotifications = imageNotifications;
    }

    public NotificationCategories() {
        this.articleNotifications = new ArticleNotifications();
        this.imageNotifications = new ImageNotifications();
    }

    public ArticleNotifications getArticles() {
        return articleNotifications;
    }

    public void setArticles(ArticleNotifications articleNotifications) {
        this.articleNotifications = articleNotifications;
    }

    public ImageNotifications getImages() {
        return imageNotifications;
    }

    public void setImages(ImageNotifications imageNotifications) {
        this.imageNotifications = imageNotifications;
    }

}
