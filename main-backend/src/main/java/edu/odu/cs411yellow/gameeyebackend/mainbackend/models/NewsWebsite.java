package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Class representing a document in the "News Websites" collection.
 */
@Document("newsWebsites")
public class NewsWebsite {
    @Id
    private final String id;

    /**
     * Title of the news website (e.g. IGN).
     */
    private String title;

    /**
     * Reference to the logo of the news website.
     */
    @DBRef
    private String logo;

    /**
     * Home page URL of the news website.
     */
    private String siteUrl;

    /**
     * RSS feed URL of the news website.
     */
    private String rssFeedUrl;

    /**
     * Date the document was last updated.
     */
    private Date lastUpdated;

    private Date publicationDate;

    @PersistenceConstructor
    public NewsWebsite(String id, String title, String logo, String siteUrl,
                       String rssFeedUrl, Date lastUpdated, Date publicationDate) {
        this.id = id;
        this.title = title;
        this.logo = logo;
        this.siteUrl = siteUrl;
        this.rssFeedUrl = rssFeedUrl;
        this.lastUpdated = lastUpdated;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
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

    public Date getPublicationDate() {
        return this.publicationDate;
    }
}
