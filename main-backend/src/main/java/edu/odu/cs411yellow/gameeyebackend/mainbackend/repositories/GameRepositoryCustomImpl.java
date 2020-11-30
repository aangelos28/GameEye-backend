package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import com.mongodb.client.result.UpdateResult;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Custom method implementations for game repository.
 */
public class GameRepositoryCustomImpl implements GameRepositoryCustom {

    private final MongoOperations mongo;
    private final MongoTemplate template;

    @Autowired
    public GameRepositoryCustomImpl(MongoOperations mongo, MongoTemplate template) {
        this.mongo = mongo;
        this.template = template;
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

    private static class GameIgdbId {
        private String igdbId;

        @PersistenceConstructor
        public GameIgdbId(String igdbId) {
            this.igdbId = igdbId;
        }

        public String getIgdbId() {
            return igdbId;
        }

        public void setIgdbId(String igdbId) {
            this.igdbId = igdbId;
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

    @Override
    public List<String> findAllIgdbIds() {
        Query query = new Query();
        query.fields().include("igdbId");

        List<GameIgdbId> gameIds = mongo.findDistinct(query,"igdbId", "games",GameIgdbId.class);

        return gameIds.stream().map(GameIgdbId::getIgdbId).collect(toList());
    }

    @Override
    public int bulkUpdateGames(List<Game> games) {
        BulkOperations ops = template.bulkOps(BulkOperations.BulkMode.UNORDERED, Game.class,"games");
        for (Game game: games) {
            Query query = new Query(Criteria.where("igdbId").is(game.getIgdbId()));
            Update update = new Update()
                    .set("title", game.getTitle())
                    .set("logoUrl", game.getLogoUrl())
                    .set("platforms", game.getPlatforms())
                    .set("genres", game.getGenres())
                    .set("releaseDate", game.getReleaseDate())
                    .set("sourceUrls", game.getSourceUrls())
                    .set("lastUpdated", new Date());

            ops.updateOne(query, update);
        }

        return ops.execute().getModifiedCount();
    }

    @Override
    public List<Article> findArticles(final String gameId) {
        Query query = new Query(new Criteria("_id").is(gameId));

        return mongo.findDistinct(query, "resources.articles",Game.class, Article.class);
    }

    @Override
    public String deleteArticlesFromGameById(String gameId) {
        Query query = new Query(Criteria.where("_id").is(gameId));
        Update update = new Update()
                .set("resources.articles", new ArrayList<>())
                .set("lastUpdated", new Date());

        mongo.updateFirst(query, update, Game.class);

        return String.format("Deleted all articles from game with id %s.", gameId);
    }

    @Override
    public String deleteArticlesFromGameByTitle(String title) {
        Query query = new Query(Criteria.where("title").is(title));
        Update update = new Update()
                .set("resources.articles", new ArrayList<>())
                .set("lastUpdated", new Date());

        UpdateResult result = mongo.updateMulti(query, update, Game.class);

        return String.format("Deleted all articles from %1$s games.", result.getModifiedCount());
    }

    @Override
    public String deleteArticlesFromAllGames() {
        Query query = new Query();
        Update update = new Update()
                .set("resources.articles", new ArrayList<>())
                .set("lastUpdated", new Date());

        UpdateResult result = mongo.updateMulti(query, update, Game.class);

        return String.format("Deleted all articles from %1$s games out of %2$s total games.",
                              result.getModifiedCount(), result.getMatchedCount());
    }
}
