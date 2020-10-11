package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * REST API for interacting with game data.
 */
@RestController
public class GameController {
    ElasticGameRepository elasticGames;

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
        public String gameTitle;
        public String gameId;

        public GameTitleAutocompletionResponse(String gameTitle, String gameId) {
            this.gameTitle = gameTitle;
            this.gameId = gameId;
        }
    }

    @Autowired
    public GameController(ElasticGameRepository elasticGames) {
        this.elasticGames = elasticGames;
    }

    /**
     * Returns a list of autocompletions for a game title.
     *
     * @param request HTTP request body.
     * @return List of autocompletions
     */
    @GetMapping(path = "/private/game/complete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameTitleAutocompletionResponse>> getTitleCompletions(@RequestBody GameTitleAutocompletionRequest request) {
        // Autocomplete title
        SearchHits<ElasticGame> searchHits = elasticGames.autocompleteGameTitle(request.gameTitle, 5);

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
}
