package edu.odu.cs411yellow.gameeyebackend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("newsWebsites")
public class NewsWebsite {
    @Id
    private String id;

    private String title;

    @DBRef
    private Image logo;

    private String siteUrl;

    private String rssFeedUrl;

    private Date lastUpdated;

    @PersistenceConstructor
    public NewsWebsite(String id, String title, Image logo, String siteUrl, String rssFeedUrl, Date lastUpdated) {
        this.id = id;
        this.title = title;
        this.logo = logo;
        this.siteUrl = siteUrl;
        this.rssFeedUrl = rssFeedUrl;
        this.lastUpdated = lastUpdated;
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

    public Image getLogo() {
        return this.logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public String getSiteUrl() {
        return this.siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getRssFeedUrl() {
        return this.rssFeedUrl;
    }

    public void setRssFeedUrl(String rssFeedUrl) {
        this.rssFeedUrl = rssFeedUrl;
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
