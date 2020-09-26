package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

public class Resources {

    private List<ImageResource> imageResources;

    private List<Article> articles;

    @PersistenceConstructor
    public Resources(List<ImageResource> imageResources, List<Article> articles) {
        this.imageResources = imageResources;
        this.articles = articles;
    }
    public Resources() {
        this.imageResources = new ArrayList<>();
        this.articles = new ArrayList<>();
    }

    public List<ImageResource> getImageResources() {
        return this.imageResources;
    }

    public void setImageResources(List<ImageResource> imageResources) {
        this.imageResources = imageResources;
    }

    public List<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Article findArticle(String id) {
        Article foundArticle = new Article();

        for (Article article : articles) {
            if (article.getId().equals(id))
                foundArticle = article;
        }

        return foundArticle;
    }

}