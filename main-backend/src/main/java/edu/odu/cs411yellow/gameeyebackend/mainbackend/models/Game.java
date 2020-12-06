package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;

import org.bson.codecs.pojo.annotations.BsonProperty;
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
    private String id;

    private String igdbId;

    private String title;

    private List<String> platforms;

    private Date releaseDate;

    private String logoUrl;

    private Date lastUpdated;

    private List<String> genres;

    private SourceUrls sourceUrls;

    private Resources resources;

    @Indexed
    private int watchers;

    @PersistenceConstructor
    public Game(String id, String igdbId, String title, List<String> platforms, Date releaseDate, String logoUrl,
                Date lastUpdated, List<String> genres, SourceUrls sourceUrls, Resources resources, int watchers) {
        this.id = id;
        this.igdbId = igdbId;
        this.title = title;
        this.platforms = platforms;
        this.releaseDate = releaseDate;
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
        this.releaseDate = null;
        this.logoUrl = "";
        this.lastUpdated = null;
        this.genres = new ArrayList<>();
        this.sourceUrls = new SourceUrls();
        this.resources = new Resources();
        this.watchers = 0;
    }

    public Game(GameResponse game) {
        this.id = "";
        this.igdbId = game.igdbId;
        this.title = game.title;
        this.platforms = game.getPlatforms();

        if (game.firstReleaseDateInSeconds != 0) {
            this.releaseDate = new Date(game.firstReleaseDateInSeconds * 1000);
        } else {
            this.releaseDate = null;
        }
        String logoUrl = "";

        // Convert UNIX epoch timestamp from IGDB to year, month, day format
        this.lastUpdated = new Date(game.lastUpdatedInSeconds * 1000);
        this.genres = game.getGenres();
        this.sourceUrls = game.getSourceUrls();
        this.resources = new Resources();
        int watchers = 0;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public List<Article> findArticles(List<String> articleIds) {
        List<Article> foundArticles = new ArrayList<>();

        for (String articleId: articleIds) {
            foundArticles.add(this.resources.findArticle(articleId));
        }

        return foundArticles;
    }

    public void addArticlesToResources(List<Article> articles) {
        resources.addArticles(articles);
    }

    public void addArticleResource(Article article) {
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
                && Objects.equals(releaseDate, that.releaseDate)
                && Objects.equals(logoUrl, that.logoUrl)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && Objects.equals(genres, that.genres)
                && Objects.equals(sourceUrls, that.sourceUrls)
                && Objects.equals(resources, that.resources)
                && Objects.equals(watchers, that.watchers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,igdbId,title,platforms, releaseDate,logoUrl,genres,sourceUrls,resources);
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
