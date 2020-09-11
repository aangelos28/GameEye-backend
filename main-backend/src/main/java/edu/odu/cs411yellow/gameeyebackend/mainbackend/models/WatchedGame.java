package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.watchedgame.NotificationCategory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public class WatchedGame {
    @DBRef
    private String gameId;

    private Integer notificationCount;

    private List<NotificationCategory> notificationCategories;

    @PersistenceConstructor
    public WatchedGame(String gameId, Integer notificationCount,
                       List<NotificationCategory> notificationCategories) {
        this.gameId = gameId;
        this.notificationCount = notificationCount;
        this.notificationCategories = notificationCategories;
    }

    public String getGameId () {return this.gameId;}

    public void setGameId (String gameId) {this.gameId = gameId;}

    public Integer getNotificationCount () {return this.notificationCount;}

    public void setTitle(Integer notificationCount) {this.notificationCount = notificationCount;}

    public List<NotificationCategory> getNotificationCategories () {return this.notificationCategories;}

    public void setNotificationCategories(List<NotificationCategory> notificationCategories) {
        this.notificationCategories = notificationCategories;
    }

}
