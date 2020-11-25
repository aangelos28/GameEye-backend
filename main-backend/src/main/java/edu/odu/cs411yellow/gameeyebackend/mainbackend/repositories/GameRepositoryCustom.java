package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Custom methods for game repository.
 */
@Repository
public interface GameRepositoryCustom {
    void incrementWatchers(String gameId);
    void decrementWatchers(String gameId);
    List<Game> findTopGames(int maxResults);
    String findTitleById(String id);
    void updateGameTitle(String id, String title);
    String findGameIdByIgdbId(String igdbId);
    void updateLogoPlatformsReleaseDateGenresSourceUrls(String id, String logoUrl, List<String> platforms,
                                                               List<String> genres, Date releaseDate, SourceUrls sourceUrls);
    void updateLastUpdatedField(String gameId);
    List<String> findAllIgdbIds();
    int bulkUpdateGames(List<Game> games);
    List<Article> findArticles(String gameId);
}
