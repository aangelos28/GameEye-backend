package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

public class Resources {

    private List<Image> images;

    private List<Article> articles;

    @PersistenceConstructor
    public Resources(List<Image> images, List<Article> articles) {
        this.images = images;
        this.articles = articles;
    }
    public Resources() {
        this.images = new ArrayList<>();
        this.articles = new ArrayList<>();
    }

    public List<Image> getImages() {
        return this.images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Article> getAllArticles() {
        return this.articles;
    }

    public void setAllArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Article getArticleById(String id) {
        Article foundArticle = new Article();

        for (Article article: articles) {
            if (article.getId() == id);
                foundArticle = article;
        }

        return foundArticle;
    }

    public List<Article> getArticlesByIds(List<String> articleResourceIds) {
        List<Article> foundArticles = new ArrayList<>();

        for (String articleId: articleResourceIds) {
            foundArticles.add(getArticleById(articleId));
        }
        return foundArticles;
    }

}