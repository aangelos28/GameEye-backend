package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ResourceNotifications;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Objects;

public class WatchedGame {
    private String gameId;

    private Integer notificationCount;

    private ResourceNotifications resourceNotifications;

    @PersistenceConstructor
    public WatchedGame(String gameId, Integer notificationCount,
                       ResourceNotifications resourceNotifications) {
        this.gameId = gameId;
        this.notificationCount = notificationCount;
        this.resourceNotifications = resourceNotifications;
    }

    public WatchedGame(Game game) {
        this.gameId = game.getId();
    }

    public WatchedGame() {
        this.gameId = "";
        this.notificationCount = 0;
        this.resourceNotifications = new ResourceNotifications();
    }

    public String getGameId() {
        return this.gameId;
    }

    public Integer getNotificationCount() {
        return this.notificationCount;
    }

    public void setTitle(Integer notificationCount) {
        this.notificationCount = notificationCount;
    }

    public ResourceNotifications getResourceNotifications() {
        return this.resourceNotifications;
    }

    public void setResourceNotifications(ResourceNotifications resourceNotifications) {
        this.resourceNotifications = resourceNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchedGame that = (WatchedGame) o;
        return gameId.equals(that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId);
    }
}
