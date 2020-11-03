package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a document in the "Games" database collection.
 */
@Document("games")
public class Game {
    @Id
    private final String id;

    private String igdbId;

    private String title;

    private List<String> platforms;

    private String status;

    private String logoUrl;

    private Date lastUpdated;

    private List<String> genres;

    private SourceUrls sourceUrls;

    private Resources resources;

    @Indexed
    private int watchers;

    @PersistenceConstructor
    public Game(String id, String igdbId, String title, List<String> platforms, String status, String logoUrl,
                Date lastUpdated, List<String> genres, SourceUrls sourceUrls, Resources resources, int watchers) {
        this.id = id;
        this.igdbId = igdbId;
        this.title = title;
        this.platforms = platforms;
        this.status = status;
        this.logoUrl = logoUrl;
        this.lastUpdated = lastUpdated;
        this.genres = genres;
        this.sourceUrls = sourceUrls;
        this.resources = resources;
        this.watchers = watchers;
    }
    public Game() {
        this.id = "";
        this.igdbId = "";
        this.title = "";
        this.platforms = new ArrayList<>();
        this.status = "";
        this.logoUrl = "";
        this.lastUpdated = new Date();
        this.genres = new ArrayList<>();
        this.sourceUrls = new SourceUrls();
        this.resources = new Resources();
        this.watchers = 0;
    }

    public String getId() {
        return this.id;
    }

    public String getIgdbId() {
        return igdbId;
    }

    public void setIgdbId(String igdbId) {
        this.igdbId = igdbId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPlatforms() {
        return this.platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Date getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<String> getGenres() {
        return this.genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public SourceUrls getSourceUrls() {
        return this.sourceUrls;
    }

    public void setSourceUrls(SourceUrls sourceUrls) {
        this.sourceUrls = sourceUrls;
    }

    public Resources getResources() {
        return this.resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public int getWatchers() {
        return this.watchers;
    }

    public List<Article> findArticles(List<String> articleIds) {
        List<Article> foundArticles = new ArrayList<>();

        for (String articleId: articleIds) {
            foundArticles.add(this.resources.findArticle(articleId));
        }

        return foundArticles;
    }

    public void addArticlesToResources(List<Article> articles) {
        resources.setArticles(articles);
    }

    public void addArticleResources(Article article) {
        resources.addArticle(article);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o ) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game that = (Game) o;

        return Objects.equals(id, that.id)
                && Objects.equals(igdbId, that.igdbId)
                && Objects.equals(title, that.title)
                && Objects.equals(platforms, that.platforms)
                && Objects.equals(status, that.status)
                && Objects.equals(logoUrl, that.logoUrl)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && Objects.equals(genres, that.genres)
                && Objects.equals(sourceUrls, that.sourceUrls)
                && Objects.equals(resources, that.resources)
                && Objects.equals(watchers, that.watchers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,igdbId,title,platforms,status,logoUrl,genres,sourceUrls,resources);
    }
}
