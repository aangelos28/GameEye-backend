package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.GameImage;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

public class Resources {

    private List<GameImage> images;

    private List<Article> articles;

    @PersistenceConstructor
    public Resources(List<GameImage> images, List<Article> articles) {
        this.images = images;
        this.articles = articles;
    }
    public Resources() {
        this.images = new ArrayList<>();
        this.articles = new ArrayList<>();
    }

    public List<GameImage> getImages() {
        return this.images;
    }

    public void setImages(List<GameImage> images) {
        this.images = images;
    }

    public List<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Article findArticle(String id) {
        Article foundArticle = new Article();

        for (Article article: articles) {
            if (article.getId() == id);
                foundArticle = article;
        }

        return foundArticle;
    }

}