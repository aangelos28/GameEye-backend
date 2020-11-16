package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Custom method implementations for game repository.
 */
public class GameRepositoryCustomImpl implements GameRepositoryCustom {

    private final MongoOperations mongo;

    @Autowired
    public GameRepositoryCustomImpl(MongoOperations mongo) {
        this.mongo = mongo;
    }

    /**
     * Increment the number of watchers of a game.
     * @param gameId The id of the game
     */
    @Override
    public void incrementWatchers(final String gameId) {
        mongo.updateFirst(new Query(Criteria.where("_id").is(gameId)), new Update().inc("watchers", 1), Game.class);
    }

    /**
     * Decrement the number of watchers of a game.
     * @param gameId The id of the game
     */
    @Override
    public void decrementWatchers(final String gameId) {
        mongo.updateFirst(new Query(Criteria.where("_id").is(gameId)), new Update().inc("watchers", -1), Game.class);
    }

    @Override
    public List<Game> findTopGames(int maxResults) {
        Query sortByWatchersQuery = new Query();
        sortByWatchersQuery.with(Sort.by(Sort.Direction.DESC, "watchers"));
        sortByWatchersQuery.limit(maxResults);
        sortByWatchersQuery.fields().include("title")
                                    .include("watchers");

        return mongo.find(sortByWatchersQuery, Game.class);
    }

    @Override
    public List<Article> findArticles(final String gameId) {
        Query query = new Query(new Criteria("_id").is(gameId));

        return mongo.findDistinct(query, "resources.articles",Game.class, Article.class);
    }
}
