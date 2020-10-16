package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses;

import java.util.Objects;

/**
 * Response for watchlist game entries.
 */
public class WatchedGameResponse {
    private String gameId;
    private String gameTitle;
    private String logoUrl;
    private Integer notificationCount;

    public WatchedGameResponse(String gameId, String gameTitle, String logoUrl, Integer notificationCount) {
        this.gameId = gameId;
        this.gameTitle = gameTitle;
        this.logoUrl = logoUrl;
        this.notificationCount = notificationCount;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public Integer getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(Integer notificationCount) {
        this.notificationCount = notificationCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchedGameResponse that = (WatchedGameResponse) o;
        return gameId.equals(that.gameId) &&
                Objects.equals(gameTitle, that.gameTitle) &&
                Objects.equals(logoUrl, that.logoUrl) &&
                notificationCount.equals(that.notificationCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, gameTitle, logoUrl, notificationCount);
    }
}
