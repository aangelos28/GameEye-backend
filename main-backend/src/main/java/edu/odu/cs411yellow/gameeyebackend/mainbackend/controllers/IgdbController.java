package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbReplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * Request for replicating a single game by title.
     */
    public static class TitleRequest {
        public String title;
    }

    /**
     * Replicates a single game from IGDB to the GameEye database.
     * Accepts the title of the game to replicate.
     * @param request HTTP request body
     */
    @PostMapping(path = "/private-admin/igdb/replicate/title", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> replicateIgdbByRange(@RequestBody TitleRequest request) {
        try {
            String result = igdbReplicationService.replicateGameByTitle(request.title);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to replicate game.");
        }
    }
}
