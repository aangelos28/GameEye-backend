package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Custom methods for game repository.
 */
@Repository
public interface GameRepositoryCustom {
    void incrementWatchers(String gameId);
    void decrementWatchers(String gameId);
    List<Game> findTopGames(int maxResults);
    List<Article> findArticles(String gameId);
}
