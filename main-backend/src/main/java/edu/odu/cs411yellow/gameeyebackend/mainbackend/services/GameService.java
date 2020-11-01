package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides services for interacting with games.
 */
@Service
public class GameService {

    GameRepository games;

    @Autowired
    GameService(GameRepository games) {
        this.games = games;
    }

    public String getLogoUrl(String gameId) {
        return games.findGameById(gameId).getLogoUrl();
    }

    public List<Article> getArticles(String gameId) {
        return games.findGameById(gameId).getResources().getArticles();
    }

    public List<Game> getTopGames(int maxResults) {
        return games.findTopGames(maxResults) ;
    }

    public boolean existsById(String id) {
        return games.existsById(id);
    }

    public void save(Game game) {
        games.save(game);
    }

    public Game findByTitle(String title) {
        return games.findByTitle(title);
    }
}
