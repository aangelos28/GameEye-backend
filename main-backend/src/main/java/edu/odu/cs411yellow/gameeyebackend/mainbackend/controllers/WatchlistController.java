package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.WatchlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WatchlistController {

    WatchlistService watchlistService;
    Logger logger = LoggerFactory.getLogger(WatchlistController.class);

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    /**
     * Gets all the games of a user's watchlist.
     *
     * @return List of games.
     */
    @GetMapping(path = "/private/watchlist")
    public List<WatchedGame> getWatchlistGames() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();

        return this.watchlistService.getWatchlistGames(fbToken.getUid());
    }

    /**
     * Adds a game to a user's watchlist.
     *
     * @param gameId Id of the game to add.
     */
    @PostMapping(path = "/private/watchlist/add/{gameId}")
    public ResponseEntity<?> addWatchlistGame(@PathVariable String gameId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();

        try {
            this.watchlistService.addWatchlistGame(fbToken.getUid(), gameId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Added game to watchlist.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to add game to watchlist.");
        }
    }

    /**
     * Deletes a game from a user's watchlist.
     *
     * @param gameIndex Index of the game in the watchlist to delete.
     */
    @DeleteMapping(path = "/private/watchlist/delete/{gameIndex}")
    public ResponseEntity<?> deleteWatchlistGame(@PathVariable int gameIndex) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();

        try {
            this.watchlistService.deleteWatchlistGame(fbToken.getUid(), gameIndex);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted game from watchlist.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete game with specified index");
        }
    }
}
