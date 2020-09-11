package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

public class Resources {
    @Id
    private String id;

    private List<String> images;

    private List<String> articles;

    private List<String> tweets;

    @PersistenceConstructor
    public Resources(String id, List<String> images, List<String> articles, List<String> tweets) {
        this.images = images;
        this.articles = articles;
        this.tweets = tweets;
    }

    public String getId() {
        return this.id;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getArticles() {
        return this.articles;
    }

    public void setArticles(List<String> articles) {
        this.articles = articles;
    }

    public List<String> getTweets() {
        return this.tweets;
    }

    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }
}