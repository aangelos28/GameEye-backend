package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationCategories;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class WatchedGame {
    private String gameId;

    private Integer notificationCount;

    private NotificationCategories notificationCategories;

    @PersistenceConstructor
    public WatchedGame(String gameId, Integer notificationCount,
                       NotificationCategories notificationCategories) {
        this.gameId = gameId;
        this.notificationCount = notificationCount;
        this.notificationCategories = notificationCategories;
    }
    public WatchedGame() {
        this.gameId = "";
        this.notificationCount = 0;
        this.notificationCategories = new NotificationCategories();
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

    public NotificationCategories getNotificationCategories() {
        return this.notificationCategories;
    }

    public void setNotificationCategories(NotificationCategories notificationCategories) {
        this.notificationCategories = notificationCategories;
    }

}
