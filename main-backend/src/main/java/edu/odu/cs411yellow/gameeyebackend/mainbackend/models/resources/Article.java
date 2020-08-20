package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

public class Article {
    @Id
    private String id;

    private String title;

    private String url;

    @DBRef
    private NewsWebsite newsWebsite;

    @DBRef
    private Image thumbnail;

    private String snippet;

    private Date lastUpdated;

    private int importanceScore;

    @PersistenceConstructor
    public Article(String id, String title, String url, NewsWebsite newsWebsite, Image thumbnail, String snippet,
                   Date lastUpdated, int importanceScore) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.newsWebsite = newsWebsite;
        this.thumbnail = thumbnail;
        this.snippet = snippet;
        this.lastUpdated = lastUpdated;
        this.importanceScore = importanceScore;
    }

    ///////////////////////////////////////////////
    // Getters/Setters
    ///////////////////////////////////////////////
    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NewsWebsite getNewsWebsite() {
        return this.newsWebsite;
    }

    public void setNewsWebsite(NewsWebsite newsWebsite) {
        this.newsWebsite = newsWebsite;
    }

    public Image getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSnippet() {
        return this.snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getImportanceScore() {
        return this.importanceScore;
    }

    public void setImportanceScore(int importanceScore) {
        this.importanceScore = importanceScore;
    }
}
