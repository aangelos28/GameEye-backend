package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Util {

    @Autowired
    private GameRepository gameRepository;

    public List<Article> findArticles(String gameId, List<String> articleIds) {

        Game game = gameRepository.findGameById(gameId);
        Resources resources = game.getResources();

        List<Article> foundArticles = new ArrayList<>();

        for (String articleId: articleIds) {
            foundArticles.add(resources.findArticle(articleId));
        }

        return foundArticles;
    }
}
