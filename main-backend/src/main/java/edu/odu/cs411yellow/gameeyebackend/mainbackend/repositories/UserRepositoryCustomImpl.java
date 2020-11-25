package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Custom method implementations for user repository.
 */
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
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

        mongo.updateFirst(query, update, User.class);
    }

    public void addArticleNotificationsToUsers(String gameId, List<Article> articles) {
        Criteria criteria = Criteria.where("watchList").elemMatch(Criteria.where("gameId").is(gameId));
        Query query = new Query();
        query.addCriteria(criteria);

        Update update = new Update();
        update.addToSet("watchList.$.resourceNotifications.articleNotifications.articleIds").each(articles.stream().map(Article::getId).collect(toList()));

        mongo.updateMulti(query, update, User.class);
    }
}
