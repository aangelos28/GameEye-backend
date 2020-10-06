package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.requests;

/**
 * Watchlist game request class.
 * Used in all administrative requests to the watchlist API.
 */
public class WatchlistGameRequest {
    private final String userId;
    private final String gameId;

    public WatchlistGameRequest(String userId, String gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGameId() {
        return gameId;
    }
}
