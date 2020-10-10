package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

/**
 * Class representing a document in the "News Websites" collection.
 */
@Document("newsWebsites")
public class NewsWebsite {
    @Id
    private final String id;

    /**
     * Name of the news website (e.g. IGN).
     */
    private String name;

    /**
     * Logo image of the news website.
     */
    private Binary logo;

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

    @PersistenceConstructor
    public NewsWebsite(String id, String name, Binary logo, String siteUrl,
                       String rssFeedUrl, Date lastUpdated) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.siteUrl = siteUrl;
        this.rssFeedUrl = rssFeedUrl;
        this.lastUpdated = lastUpdated;
    }

    public NewsWebsite() {
        this.id = "";
        this.name = "";
        this.logo = new Binary(new byte[1]);
        this.siteUrl = "";
        this.rssFeedUrl = "";
        this.lastUpdated = new Date();
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Binary getLogo() {
        return this.logo;
    }

    public void setLogo(Binary logo) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o ) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsWebsite that = (NewsWebsite) o;

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(logo, that.logo)
                && Objects.equals(siteUrl, that.siteUrl)
                && Objects.equals(rssFeedUrl, that.rssFeedUrl)
                && Objects.equals(lastUpdated, that.lastUpdated);
    }
}
