package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
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
    public String findTitleById(final String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        query.fields().include("title");

        return mongo.findOne(query, String.class, "games");
    }

    @Override
    public void updateGameTitle(final String id, final String title) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("title", title);

        mongo.updateFirst(query, update, Game.class);
        updateLastUpdatedField(id);
    }

    private static class GameId {
        @Id
        private String id;

        @PersistenceConstructor
        public GameId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Override
    public String findGameIdByIgdbId(final String igdbId) {
        Query query = new Query(Criteria.where("igdbId").is(igdbId));
        query.fields().include("_id");

        return mongo.findOne(query, GameId.class, "games").getId();
    }

    @Override
    public void updateLogoPlatformsReleaseDateGenresSourceUrls(final String id, final String logoUrl, final List<String> platforms,
                                                               final List<String> genres, final Date releaseDate, final SourceUrls sourceUrls) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update()
                .set("logoUrl", logoUrl)
                .set("platforms", platforms)
                .set("genres", genres)
                .set("releaseDate", releaseDate)
                .set("sourceUrls", sourceUrls);

        mongo.updateFirst(query, update, Game.class);
        updateLastUpdatedField(id);
    }

    @Override
    public void updateLastUpdatedField(String gameId) {
        Query query = new Query(Criteria.where("_id").is(gameId));
        Update update = new Update().set("lastUpdated", new Date());

        mongo.updateFirst(query, update, Game.class);
    }
}
