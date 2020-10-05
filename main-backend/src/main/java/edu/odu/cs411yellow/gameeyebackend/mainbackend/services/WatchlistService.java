package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ResourceNotifications;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing user watchlists.
 */
@Service
public class WatchlistService {
    UserRepository users;
    GameRepository games;

    Logger logger = LoggerFactory.getLogger(WatchlistService.class);

    @Autowired
    public WatchlistService(UserRepository users, GameRepository games) {
        this.users = users;
        this.games = games;
    }

    /**
     * Get the watchlist of a user.
     *
     * @param firebaseUserId The firebase id of the user.
     * @return List of watched games.
     */
    public List<WatchedGame> getWatchlistGames(String firebaseUserId) {
        User user = this.users.findUserByFirebaseId(firebaseUserId);

        return user.getWatchList();
    }

    /**
     * Add a game to a user's watchlist.
     *
     * @param firebaseUserId The firebase id of the user.
     * @param gameId         Id of the game to add.
     */
    public void addWatchlistGame(String firebaseUserId, String gameId) {
        User user = this.users.findUserByFirebaseId(firebaseUserId);
        Game game = this.games.findGameById(gameId);

        if (game == null) return;

        List<WatchedGame> watchlist = user.getWatchList();
        WatchedGame newGame = new WatchedGame(game.getId(), 0, new ResourceNotifications());

        // Ensure we do not add a duplicate game
        if (!watchlist.contains(newGame)) {
            // Add game to watchlist
            watchlist.add(newGame);
            users.save(user);
        }
    }

    /**
     * Delete a game from a user's watchlist.
     *
     * @param firebaseUserId The firebase id of the user.
     * @param gameIndex      Index of the game in the watchlist to delete.
     */
    public void deleteWatchlistGame(String firebaseUserId, int gameIndex) throws Exception {
        User user = this.users.findUserByFirebaseId(firebaseUserId);

        // Disallow negative game indices
        if (gameIndex < 0) {
            throw new Exception("Negative game indices not allowed.");
        }

        List<WatchedGame> watchlist = user.getWatchList();

        // Disallow game indices out of watchlist bounds
        if (gameIndex >= watchlist.size()) {
            throw new Exception(String.format("Game index %d out of bounds for watchlist of size %d", gameIndex, watchlist.size()));
        }

        // Remove game from watchlist
        watchlist.remove(gameIndex);
        users.save(user);
    }
}
