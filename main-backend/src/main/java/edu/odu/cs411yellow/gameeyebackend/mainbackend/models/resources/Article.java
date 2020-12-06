package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a news article resource.
 */
public class Article {
    @BsonProperty("id")
    private String id;

    /**
     * Title of the article.
     */
    private String title;

    /**
     * URL to the page containing the article.
     */
    private String url;

    /**
     * Name of the news website from which this article was retrieved.
     */
    private String newsWebsiteName;

    /**
     * Reference to a thumbnail for this article, if any.
     */
    private String thumbnailId;

    /**
     * Small portion of the text body of an article, usually the first few sentences.
     * Limit to 255 characters.
     */
    private String snippet;

    /**
     * Date the article was published.
     */
    private Date publicationDate;

    /**
     * Date the document was last updated.
     */
    private Date lastUpdated;

    /**
     * Denotes whether or not an article is important, computed with machine learning.
     */
    private boolean isImportant;

    @PersistenceConstructor
    public Article(String id, String title, String url, String newsWebsiteName, String thumbnailId, String snippet,
                   Date publicationDate, Date lastUpdated, boolean isImportant) {

        if (id == null || id.equals("")) {
            this.id = ObjectId.get().toHexString();
        }
        this.id = id;
        this.title = title;
        this.url = url;
        this.newsWebsiteName = newsWebsiteName;
        this.thumbnailId = thumbnailId;
        this.snippet = snippet;
        this.publicationDate = publicationDate;
        this.lastUpdated = lastUpdated;
        this.isImportant = isImportant;
    }

    public Article() {
        this.id = ObjectId.get().toHexString();
        this.title = "";
        this.url = "";
        this.newsWebsiteName = "";
        this.thumbnailId = "";
        this.snippet = "";
        this.publicationDate = new Date();
        this.lastUpdated = new Date();
        this.isImportant = false;
    }

    public Article(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.url = article.getUrl();
        this.newsWebsiteName = article.getNewsWebsiteName();
        this.thumbnailId = article.getThumbnailId();
        this.snippet = article.getSnippet();
        this.publicationDate = article.getPublicationDate();
        this.lastUpdated = article.getLastUpdated();
        this.isImportant = article.getIsImportant();
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

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNewsWebsiteName() {
        return this.newsWebsiteName;
    }

    public void setNewsWebsiteName(String newsWebsiteName) {
        this.newsWebsiteName = newsWebsiteName;
    }

    public String getThumbnailId() {
        return this.thumbnailId;
    }

    public void setThumbnailId(String thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getSnippet() {
        return this.snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Date getPublicationDate() {
        return this.publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean getIsImportant() {
        return this.isImportant;
    }

    public void setIsImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o ) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article that = (Article) o;

        return Objects.equals(title, that.title)
                && Objects.equals(newsWebsiteName, that.newsWebsiteName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, newsWebsiteName, thumbnailId, snippet, publicationDate, isImportant);
    }

    @Override
    public String toString() {
        ObjectMapper obj= new ObjectMapper();
        String result;

        try {
            result = obj.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result = "JsonProcessingException";
        }

        return result;
    }
}
