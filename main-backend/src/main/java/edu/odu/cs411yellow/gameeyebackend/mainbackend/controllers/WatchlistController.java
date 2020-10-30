package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.requests.WatchlistGameRequest;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses.WatchedGameResponse;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.WatchlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for interacting with user watchlists.
 */
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
    @GetMapping(path = "/private/watchlist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWatchlistGames(@RequestParam(required = false) boolean brief) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        if (brief) {
            return ResponseEntity.ok(watchlistService.getWatchlistGamesShort(userId));
        } else {
            return ResponseEntity.ok(watchlistService.getWatchlistGames(userId));
        }
    }

    /**
     * Returns the ith game in a user's watchlist.
     *
     * @param index Index of the game in the watchlist to get
     * @return Watched game under index
     */
    @GetMapping(path = "/private/watchlist/game/{index}")
    public ResponseEntity<?> getWatchlistGame(@PathVariable int index, @RequestParam(required = false) boolean brief) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        if (brief) {
            return ResponseEntity.ok(watchlistService.getWatchlistGameShort(userId, index));
        } else {
            return ResponseEntity.ok(watchlistService.getWatchlistGame(userId, index));
        }
    }

    /**
     * Adds a game to a user's watchlist.
     *
     * @param request HTTP request body
     */
    @PostMapping(path = "/private/watchlist/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addWatchlistGame(@RequestBody WatchlistGameRequest request) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();

        try {
            watchlistService.addWatchlistGame(fbToken.getUid(), request.getGameId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Added game to watchlist.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to add game to watchlist.");
        }
    }

    /**
     * Deletes a game from a user's watchlist
     *
     * @param gameIndex Index of the game in the watchlist to delete.
     */
    @DeleteMapping(path = "/private/watchlist/delete/{gameIndex}")
    public ResponseEntity<?> deleteWatchlistGameByIndex(@PathVariable int gameIndex) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();

        try {
            watchlistService.deleteWatchlistGameByIndex(fbToken.getUid(), gameIndex);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted game from watchlist.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete game with specified index");
        }
    }

    /**
     * Deletes a game with the specified id from a user's watchlist.
     *
     * @param request HTTP request body
     */
    @DeleteMapping(path = "/private/watchlist/delete")
    public ResponseEntity<?> deleteWatchlistGameById(@RequestBody WatchlistGameRequest request) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();

        try {
            watchlistService.deleteWatchlistGameById(fbToken.getUid(), request.getGameId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted game from watchlist.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete game with specified index");
        }
    }

    /**
     * Gets all the games in the watchlist of a user with a specific id.
     *
     * @return List of games.
     */
    @GetMapping(path = "/private-admin/watchlist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWatchlistGamesAdmin(@RequestBody WatchlistGameRequest request) {
        final String userId = request.getUserId();

        try {
            final List<WatchedGameResponse> watchlist = watchlistService.getWatchlistGames(userId);

            logger.info(String.format("ADMIN: Got watchlist of user %s.", userId));

            return ResponseEntity.ok().body(watchlist);
        } catch (Exception ex) {
            ex.printStackTrace();

            final String response = String.format("ADMIN: Failed to get watchlist of user %s.", userId);
            logger.warn(response);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Adds a game with a specific id to the watchlist of the user with
     * a specific id.
     *
     * @param request HTTP request body
     */
    @PostMapping(path = "/private-admin/watchlist/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addWatchlistGameAdmin(@RequestBody WatchlistGameRequest request) {
        final String userId = request.getUserId();
        final String gameId = request.getGameId();

        try {
            watchlistService.addWatchlistGame(userId, gameId);

            final String response = String.format("ADMIN: Added game %s to watchlist of user %s.", gameId, userId);
            logger.info(response);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            ex.printStackTrace();

            final String response = String.format("ADMIN: Failed to add game %s to watchlist of user %s.", userId, gameId);
            logger.warn(response);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Deletes a game from the watchlist of a user with a specific id.
     *
     * @param gameIndex Index of the game in the user's watchlist to delete
     * @param request   HTTP request body
     */
    @DeleteMapping(path = "/private-admin/watchlist/delete/{gameIndex}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteWatchlistGameAdmin(@PathVariable int gameIndex, @RequestBody WatchlistGameRequest request) {
        final String userId = request.getUserId();

        try {
            watchlistService.deleteWatchlistGameByIndex(userId, gameIndex);

            final String response = String.format("ADMIN: Deleted game %d from watchlist of user %s.", gameIndex, userId);
            logger.info(response);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception ex) {
            ex.printStackTrace();

            final String response = String.format("ADMIN: Failed to delete game %d from watchlist of user %s.", gameIndex, userId);
            logger.warn(response);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ADMIN: Failed to delete game with specified index.");
        }
    }
}
