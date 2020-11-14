package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * REST API for interacting with game data.
 */
@RestController
public class GameController {
    ElasticGameRepository elasticGames;
    GameService gameService;

    Logger logger = LoggerFactory.getLogger(GameController.class);

    /**
     * Represents an HTTP request body for game title autocompletion.
     */
    public static class GameTitleAutocompletionRequest {
        public String gameTitle;
        public int maxSuggestions;
    }

    /**
     * Represents an HTTP response body for game title autocompletion.
     */
    private static class GameTitleAutocompletionResponse {
        public String title;
        public String id;

        public GameTitleAutocompletionResponse(String title, String id) {
            this.title = title;
            this.id = id;
        }
    }

    @Autowired
    public GameController(ElasticGameRepository elasticGames, GameService gameService) {
        this.elasticGames = elasticGames;
        this.gameService = gameService;
    }

    /**
     * Returns a list of autocompletions for a game title.
     *
     * @param request HTTP request body.
     * @return List of autocompletions
     */
    @PostMapping(path = "/private/game/complete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameTitleAutocompletionResponse>> getTitleCompletions(@RequestBody GameTitleAutocompletionRequest request) {
        // Autocomplete title
        SearchHits<ElasticGame> searchHits = elasticGames.autocompleteGameTitle(request.gameTitle, Integer.min(request.maxSuggestions, 15));

        if (searchHits.getTotalHits() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        final List<GameTitleAutocompletionResponse> autocompletionResults = new ArrayList<>();
        for (SearchHit<ElasticGame> searchHit : searchHits) {
            ElasticGame game = searchHit.getContent();
            autocompletionResults.add(new GameTitleAutocompletionResponse(game.getTitle(), game.getGameId()));
        }

        return ResponseEntity.ok(autocompletionResults);
    }

    public static class LogoRequest {
        public String id;
    }

    private static class LogoResponse {
        public String url;

        public LogoResponse(String url) {
            this.url = url;
        }
    }

    /**
     * Returns a logo url for a game id.
     *
     * @param request HTTP request body.
     * @return logo url
     */
    @PostMapping(path = "/private/game/logo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLogo(@RequestBody LogoRequest request) {
        if (!gameService.existsById(request.id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game with specified id not found.");
        }

        return ResponseEntity.ok(gameService.getLogoUrl(request.id));
    }

    public static class ArticlesRequest {
        public String id;
    }

    private static class ArticlesResponse {
        public String title;
        public String url;
        public String logoString;
        public Image thumbnail;
        public String snippet;
        public Date publicationDate;
        public boolean impactScore;

        public ArticlesResponse(Article article) {
            this.title = article.getTitle();
            this.url = article.getUrl();
            this.logoString = Base64.getEncoder().encodeToString(article.getNewsWebsite().getLogo().getData());
            this.thumbnail = article.getThumbnail();
            this.snippet = article.getSnippet();
            this.publicationDate = article.getPublicationDate();
            this.impactScore = article.getIsImportant();
        }
    }

    /**
     * Returns a list of articles for a game id.
     *
     * @param request HTTP request body.
     * @return list of articles
     */
    @PostMapping(path = "/private/game/articles", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getArticles(@RequestBody ArticlesRequest request) {
        if (!gameService.existsById(request.id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game with specified id not found.");
        }

        List<Article> foundArticles = gameService.getArticles(request.id);
        final List<ArticlesResponse> articles = new ArrayList<>();
        for (Article article :foundArticles) {
            ArticlesResponse articleResponse = new ArticlesResponse(article);
            articles.add(articleResponse);
        }

        return ResponseEntity.ok(articles);
    }

    public static class TopGamesRequest {
        public int maxResults;
    }

    private static class TopGameResponse {
        public String title;
        public int watchers;

        public TopGameResponse(Game game) {
            this.title = game.getTitle();
            this.watchers = game.getWatchers();
        }
    }

    /**
     * Returns a list of the most watched games.
     *
     * @param request HTTP request body.
     * @return list of game titles and corresponding watchers value.
     */
    @PostMapping(path = "/private/game/top", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMostWatchedGames(@RequestBody TopGamesRequest request) {
        try {
            List<Game> games = gameService.getTopGames(Integer.min(request.maxResults, 50));
            List<TopGameResponse> gameResponses = new ArrayList<>();

            for (Game game: games) {
                gameResponses.add(new TopGameResponse(game));
            }

            return ResponseEntity.ok(gameResponses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Check request syntax or the games collection is empty.");
        }
    }
}
