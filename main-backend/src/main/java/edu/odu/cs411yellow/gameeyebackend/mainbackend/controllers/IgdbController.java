package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbReplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API for interacting with IGDB game data.
 */
@RestController
public class IgdbController {

    private final IgdbReplicationService igdbReplicationService;

    @Autowired
    public IgdbController(IgdbReplicationService igdbReplicationService) {
        this.igdbReplicationService = igdbReplicationService;
    }

    /**
     * Request for a range of IGDB ids and limit per API request
     * Used for admin endpoints in the IGDB API.
     */
    public static class IdRangeRequest {
        public int minId;
        public int maxId;
        public int limit;

        public IdRangeRequest() {
        }

        public IdRangeRequest(int minId, int maxId, int limit) {
            this.minId = minId;
            this.maxId = maxId;
            this.limit = limit;
        }
    }


    /**
     * Replicates game data from IGDB to the GameEye database.
     * Accepts an IGDB game id range and limit per API request.
     * @param request HTTP request body
     */
    @PostMapping(path = "/private-admin/igdb/replicate/ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> replicateIgdbByRange(@RequestBody IdRangeRequest request) {
        try {
            String result = igdbReplicationService.replicateGamesByRange(request.minId, request.maxId, request.limit);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to replicate games.");
        }
    }

    /**
     * Request for replicating games by titles.
     */
    public static class TitlesRequest {
        public List<String> titles;
        public int limit;
    }

    /**
     * Replicates a multiple games from IGDB to the GameEye database.
     * Accepts the titles of the game to replicate.
     * @param request HTTP request body containing a list of titles and a limit per API request.
     */
    @PostMapping(path = "/private-admin/igdb/replicate/titles", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> replicateIgdbByRange(@RequestBody TitlesRequest request) {
        try {
            String result = igdbReplicationService.replicateGamesByTitles(request.titles, request.limit);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to replicate game.");
        }
    }
}
