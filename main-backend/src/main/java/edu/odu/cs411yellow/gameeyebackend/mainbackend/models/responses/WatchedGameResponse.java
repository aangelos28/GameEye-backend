package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ResourceNotifications;

/**
 * Response for watchlist game entries.
 */
public class WatchedGameResponse extends WatchedGame {
    private String gameTitle;
    private String logoUrl;

    public WatchedGameResponse(String gameId, Integer notificationCount,
                               ResourceNotifications resourceNotifications, String gameTitle, String logoUrl) {
        super(gameId, notificationCount, resourceNotifications);
        this.gameTitle = gameTitle;
        this.logoUrl = logoUrl;
    }

    public WatchedGameResponse(WatchedGame watchedGame) {
        super(watchedGame);
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
