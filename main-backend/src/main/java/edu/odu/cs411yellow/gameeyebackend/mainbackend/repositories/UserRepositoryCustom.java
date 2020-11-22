package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Custom methods for UserRepository
 */
@Repository
public interface UserRepositoryCustom {
    void addArticleNotificationsToUsers(String gameId, List<Article> articles);
}
