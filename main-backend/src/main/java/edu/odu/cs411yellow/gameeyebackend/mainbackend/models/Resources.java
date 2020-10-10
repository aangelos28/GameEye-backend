package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Resources {

    private List<ImageResource> images;

    private List<Article> articles;

    @PersistenceConstructor
    public Resources(List<ImageResource> images, List<Article> articles) {
        this.images = images;
        this.articles = articles;
    }
    public Resources() {
        this.images = new ArrayList<>();
        this.articles = new ArrayList<>();
    }

    public List<ImageResource> getImages() {
        return this.images;
    }

    public void setImages(List<ImageResource> images) {
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

        for (Article article : articles) {
            if (article.getId().equals(id)) {
                foundArticle = article;
                break;
            }
        }

        return foundArticle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o ) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resources that = (Resources) o;

        return Objects.equals(articles, that.articles)
                && Objects.equals(images, that.images);
    }

}
