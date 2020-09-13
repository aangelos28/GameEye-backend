package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.springframework.data.annotation.PersistenceConstructor;
import java.util.List;

public class Resources {

    private List<String> images;

    private List<String> articles;

    @PersistenceConstructor
    public Resources(List<String> images, List<String> articles) {
        this.images = images;
        this.articles = articles;
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

}