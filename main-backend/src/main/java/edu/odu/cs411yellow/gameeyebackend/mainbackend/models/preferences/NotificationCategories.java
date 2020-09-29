package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import org.springframework.data.annotation.PersistenceConstructor;

public class NotificationCategories {

    private ArticleNotifications articleNotifications;

    private ImageCategory imageCategory;

    @PersistenceConstructor
    public NotificationCategories(ArticleNotifications articleNotifications, ImageCategory imageCategory) {
        this.articleNotifications = articleNotifications;
        this.imageCategory = imageCategory;
    }

    public NotificationCategories() {
        this.articleNotifications = new ArticleNotifications();
        this.imageCategory = new ImageCategory();
    }

    public ArticleNotifications getArticles() {
        return articleNotifications;
    }

    public void setArticles(ArticleNotifications articleNotifications) {
        this.articleNotifications = articleNotifications;
    }

    public ImageCategory getImages() {
        return imageCategory;
    }

    public void setImages(ImageCategory imageCategory) {
        this.imageCategory = imageCategory;
    }

}
