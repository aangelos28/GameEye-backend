package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Custom method implementations for user repository.
 */
public class UserRepositoryCustomImpl implements UserRepositoryCustom{
    private final MongoOperations mongo;

    @Autowired
    public UserRepositoryCustomImpl(MongoOperations mongo) {
        this.mongo = mongo;
    }

    @Override
    public void removeUserArticleNotifications(final String userId, final String gameId, List<String> articleIds) {
        Object[] articles = articleIds.toArray();

        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = new Update().pullAll("watchList.$[watchedGame].resourceNotifications.articleNotifications.articleIds",
                                                   articles)
                                    .filterArray(Criteria.where("watchedGame.gameId").is(gameId));

        UpdateResult result = mongo.updateFirst(query, update, User.class);
    }
}
