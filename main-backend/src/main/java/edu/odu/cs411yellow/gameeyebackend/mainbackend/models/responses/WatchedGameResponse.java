package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ResourceNotifications;

/**
 * Response for watchlist game entries.
 */
public class WatchedGameResponse extends WatchedGame {
    private String title;
    private String logoUrl;

    public WatchedGameResponse(String gameId, Integer notificationCount,
                               ResourceNotifications resourceNotifications, String title, String logoUrl) {
        super(gameId, notificationCount, resourceNotifications);
        this.title = title;
        this.logoUrl = logoUrl;
    }

    public WatchedGameResponse(WatchedGame watchedGame) {
        super(watchedGame);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
