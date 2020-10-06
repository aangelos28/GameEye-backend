package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.requests;

/**
 * Watchlist game request body class.
 * Used for admin endpoints in the watchlist API.
 */
public class WatchlistGameRequest {
    private String userId;
    private String gameId;

    public WatchlistGameRequest() {
    }

    public WatchlistGameRequest(String userId, String gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
