package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import org.springframework.data.annotation.PersistenceConstructor;

public class NotificationCategories {

    private ArticleCategory articleCategory;

    private ImageCategory imageCategory;

    @PersistenceConstructor
    public NotificationCategories(ArticleCategory articleCategory, ImageCategory imageCategory) {
        this.articleCategory = articleCategory;
        this.imageCategory = imageCategory;
    }

    public NotificationCategories() {
        this.articleCategory = new ArticleCategory();
        this.imageCategory = new ImageCategory();
    }

    public ArticleCategory getArticles() {
        return articleCategory;
    }

    public void setArticles(ArticleCategory articleCategory) {
        this.articleCategory = articleCategory;
    }

    public ImageCategory getImages() {
        return imageCategory;
    }

    public void setImages(ImageCategory imageCategory) {
        this.imageCategory = imageCategory;
    }

}
