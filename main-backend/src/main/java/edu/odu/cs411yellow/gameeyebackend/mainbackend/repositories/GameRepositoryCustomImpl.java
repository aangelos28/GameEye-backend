package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
}
