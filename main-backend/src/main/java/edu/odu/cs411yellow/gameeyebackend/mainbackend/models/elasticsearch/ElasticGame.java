package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.completion.Completion;

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

    @PersistenceConstructor
    public ElasticGame(String id, String gameId, String title) {
        this.id = id;
        this.gameId = gameId;
        this.title = title;
    }

    public ElasticGame(Game game) {
        this.gameId = game.getId();
        this.title = game.getTitle();
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
}
