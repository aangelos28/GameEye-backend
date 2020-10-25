package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

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
    private static class GameTitleAutocompletionRequest {
        public String gameTitle;
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
        SearchHits<ElasticGame> searchHits = elasticGames.autocompleteGameTitle(request.gameTitle, 8);

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

    private static class LogoRequest {
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
    @PostMapping(path = "/private/game/logo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LogoResponse> getLogo(@RequestBody LogoRequest request) {

        String logoUrl = gameService.getLogoUrl(request.id);
        LogoResponse logoResponse = new LogoResponse(logoUrl);

        return ResponseEntity.ok(logoResponse);
    }

    private static class ArticlesRequest {
        public String id;
    }

    private static class ArticlesResponse {
        public String title;
        public String snippet;
        public Date publicationDate;
        public int impactScore;
        public String url;

        public ArticlesResponse(String title, String snippet, Date publicationDate,
                                int impactScore, String url) {
            this.title = title;
            this.snippet = snippet;
            this.publicationDate = publicationDate;
            this.impactScore = impactScore;
            this.url = url;
        }
    }

    /**
     * Returns a list of articles for a game id.
     *
     * @param request HTTP request body.
     * @return list of articles
     */
    @PostMapping(path = "/private/game/articles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticlesResponse>> getLogo(@RequestBody ArticlesRequest request) {

        List<Article> foundArticles = gameService.getArticles(request.id);
        final List<ArticlesResponse> articles = new ArrayList<>();
        for (Article article :foundArticles) {
            ArticlesResponse articleResponse = new ArticlesResponse(article.getTitle(), article.getSnippet(),
                                                                    article.getPublicationDate(), article.getImpactScore(),
                                                                    article.getUrl());
            articles.add(articleResponse);
        }

        return ResponseEntity.ok(articles);
    }
}
