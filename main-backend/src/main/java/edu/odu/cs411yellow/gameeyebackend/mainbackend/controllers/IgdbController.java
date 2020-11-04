package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.requests.IgdbControllerRequest;
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
     * Replicates game data from IGDB to the GameEye database.
     *
     * @param request HTTP request body
     */
    @PostMapping(path = "/private-admin/igdb/replicate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> replicateIgdbByRange(@RequestBody IgdbControllerRequest request) {
        try {
            String result = igdbReplicationService.replicateIgdbByRange(request.getMinId(), request.getMaxId(), request.getLimit());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to replicate games.");
        }
    }
}
