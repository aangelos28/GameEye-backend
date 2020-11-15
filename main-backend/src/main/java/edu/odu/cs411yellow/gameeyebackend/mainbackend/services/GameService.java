package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Provides services for interacting with games.
 */
@Service
public class GameService {

    private GameRepository gameRepository;

    @Autowired
    GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public String getLogoUrl(String gameId) {
        return gameRepository.findGameById(gameId).getLogoUrl();
    }

    public List<Article> findArticles(String gameId) {
        return gameRepository.findArticles(gameId);
    }

    public List<Game> getTopGames(int maxResults) {
        return gameRepository.findTopGames(maxResults) ;
    }

    public Game findGameById(final String gameId) {
        return gameRepository.findGameById(gameId);
    }

    public boolean existsById(String id) {
        return gameRepository.existsById(id);
    }

    public void save(Game game) {
        game.setLastUpdated(new Date());

        if (game.getId().equals("")) {
            game.setId(ObjectId.get().toString());
        }

        gameRepository.save(game);
    }

    public void saveAll(List<Game> games) {
        for (Game game: games) {
            game.setLastUpdated(new Date());
        }

        gameRepository.saveAll(games);
    }

    public Game findByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    public boolean existsByIgdbId(String igdbId) {
        return gameRepository.existsByIgdbId(igdbId);
    }

    public Game findByIgdbId(String igdbId) {
        return gameRepository.findByIgdbId(igdbId);
    }
}
