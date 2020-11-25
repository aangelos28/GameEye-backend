package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.completion.Completion;

import java.util.Date;

/**
 * Represents a game title document in ElasticSearch.
 * Used for autocompletion suggestions.
 */
@Document(indexName = "games")
@Setting(settingPath = "/elasticsearch/elasticgame-config.json")
public class ElasticGame {
    /**
     * Id of the game in ElasticSearch.
     */
    @Id
    private String id;

    /**
     * Id of the game in MongoDB.
     */
    private String gameId;

    /**
     * Title of the game.
     */
    @Field(type = FieldType.Text, analyzer = "autocomplete_index", searchAnalyzer = "autocomplete_search")
    private String title;

    /**
     * Release date of the game.
     */
    @Field(type = FieldType.Date, format = DateFormat.date_time, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date releaseDate;

    /**
     * Url of the logo of the game.
     */
    private String logoUrl;

    @PersistenceConstructor
    public ElasticGame(String id, String gameId, String title, Date releaseDate, String logoUrl) {
        this.id = id;
        this.gameId = gameId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.logoUrl = logoUrl;
    }

    public ElasticGame(Game game) {
        this.gameId = game.getId();
        this.title = game.getTitle();
        this.releaseDate = game.getReleaseDate();
        this.logoUrl = game.getLogoUrl();
    }

    public ElasticGame() {
        this.gameId = "";
        this.title = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
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
}
