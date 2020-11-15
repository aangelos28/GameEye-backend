package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ElasticGameCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin REST API for interacting with ElasticSearch indices.
 */
@RestController
public class ElasticGameController {
    private final ElasticGameCreationService elasticGameCreationService;

    @Autowired
    ElasticGameController(ElasticGameCreationService elasticGameCreationService) {
        this.elasticGameCreationService = elasticGameCreationService;
    }

    /**
     * Indexes all games in the GameEye database in ElasticSearch.
     */
    @PostMapping(path = "/private-admin/elasticgames/populate")
    public ResponseEntity<String> createElasticGamesFromGames() {
        try {
            final long createdGames = elasticGameCreationService.createElasticGamesFromGames();
            return ResponseEntity.ok(String.format("Inserted %d games to ElasticSearch.", createdGames));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
