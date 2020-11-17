package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a news article resource.
 */
public class Article {
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

        if (id.equals("")) {
            this.id = ObjectId.get().toHexString();
        } else {
            this.id = id;
        }
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
        this.id = ObjectId.get().toHexString();;
        this.title = "";
        this.url = "";
        this.newsWebsiteName = "";
        this.thumbnailId = "";
        this.snippet = "";
        this.publicationDate = new Date();
        this.lastUpdated = new Date();
        this.isImportant = false;
    }

    public Article(Article a){
        this.id = a.getId();
        this.title = a.getTitle();
        this.url = a.getUrl();
        this.newsWebsiteName = a.getNewsWebsiteName();
        this.thumbnailId = a.getThumbnailId();
        this.snippet = a.getSnippet();
        this.publicationDate = a.getPublicationDate();
        this.lastUpdated = a.getLastUpdated();
        this.isImportant = a.getIsImportant();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

        /*return Objects.equals(id, that.id)
                && Objects.equals(url, that.url)
                && Objects.equals(title, that.title)
                && Objects.equals(newsWebsiteName, that.newsWebsiteName)
                && Objects.equals(thumbnailId, that.thumbnailId)
                && Objects.equals(snippet, that.snippet)
                && Objects.equals(publicationDate, that.publicationDate)
                && Objects.equals(isImportant, that.isImportant);*/

        return Objects.equals(title, that.title) && Objects.equals(newsWebsiteName, that.newsWebsiteName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, newsWebsiteName, thumbnailId, snippet, publicationDate, isImportant);
    }
}
