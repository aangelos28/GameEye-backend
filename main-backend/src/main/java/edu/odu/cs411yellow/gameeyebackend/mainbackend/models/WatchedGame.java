package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationCategories;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class WatchedGame {
    @DBRef
    private Game game;

    private Integer notificationCount;

    private NotificationCategories notificationCategories;

    @PersistenceConstructor
    public WatchedGame(Game game, Integer notificationCount,
                       NotificationCategories notificationCategories) {
        this.game = game;
        this.notificationCount = notificationCount;
        this.notificationCategories = notificationCategories;
    }
    public WatchedGame() {
        this.game = new Game();
        this.notificationCount = 0;
        this.notificationCategories = new NotificationCategories();
    }

    public Game getGame() {
        return this.game;
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
