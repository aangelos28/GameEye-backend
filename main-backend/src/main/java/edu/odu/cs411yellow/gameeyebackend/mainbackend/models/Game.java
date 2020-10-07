package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class representing a document in the "Games" database collection.
 */
@Document("games")
public class Game {
    @Id
    private final String id;

    private String igdbId;

    @Indexed(unique = true)
    private String title;

    private List<String> platforms;

    private String status;

    private Date lastUpdated;

    private List<String> genres;

    private SourceUrls sourceUrls;

    private Resources resources;

    @PersistenceConstructor
    public Game(String id, String igdbId, String title, List<String> platforms, String status, Date lastUpdated,
                List<String> genres, SourceUrls sourceUrls, Resources resources) {
        this.id = id;
        this.igdbId = igdbId;
        this.title = title;
        this.platforms = platforms;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.genres = genres;
        this.sourceUrls = sourceUrls;
        this.resources = resources;
    }
    public Game() {
        this.id = "";
        this.igdbId = "";
        this.title = "";
        this.platforms = new ArrayList<>();
        this.status = "";
        this.lastUpdated = new Date();
        this.genres = new ArrayList<>();
        this.sourceUrls = new SourceUrls();
        this.resources = new Resources();
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

    public List<Article> findArticles(List<String> articleIds) {
        List<Article> foundArticles = new ArrayList<>();

        for (String articleId: articleIds) {
            foundArticles.add(this.resources.findArticle(articleId));
        }

        return foundArticles;
    }

    //TODO
    /*@Override
    public boolean equals(Game game) {

    }*/

}
