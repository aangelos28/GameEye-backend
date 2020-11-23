package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;

/**
 * Response for watchlist game entries.
 */
public class WatchedGameResponse extends WatchedGame {
    private String title;
    private String logoUrl;
    private String releaseDate;

    private GameNotificationCounts notificationCounts;

    public WatchedGameResponse(WatchedGame watchedGame) {
        super(watchedGame);
        this.notificationCounts = new GameNotificationCounts(watchedGame);
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

    public GameNotificationCounts getNotificationCounts() {
        return notificationCounts;
    }

    public void setNotificationCounts(GameNotificationCounts notificationCounts) {
        this.notificationCounts = notificationCounts;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
