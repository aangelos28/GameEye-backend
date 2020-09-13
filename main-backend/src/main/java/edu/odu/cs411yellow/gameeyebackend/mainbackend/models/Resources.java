package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.data.annotation.PersistenceConstructor;
import java.util.List;

public class Resources {

    private List<Image> images;

    private List<Article> articles;

    @PersistenceConstructor
    public Resources(List<Image> images, List<Article> articles) {
        this.images = images;
        this.articles = articles;
    }

    public List<Image> getImages() {
        return this.images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}