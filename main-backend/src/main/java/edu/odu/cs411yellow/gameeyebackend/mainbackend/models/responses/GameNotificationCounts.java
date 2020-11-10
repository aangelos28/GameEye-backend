package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.notifications.ResourceNotifications;

public class GameNotificationCounts {
    private int totalNotifications;
    private int articleNotifications;

    public GameNotificationCounts(int totalNotifications, int articleNotifications) {
        this.totalNotifications = totalNotifications;
        this.articleNotifications = articleNotifications;
    }

    public GameNotificationCounts(final WatchedGame watchedGame) {
        final ResourceNotifications resourceNotifications = watchedGame.getResourceNotifications();
        this.articleNotifications = resourceNotifications.getArticleNotifications().getArticleIds().size();

        this.totalNotifications = this.articleNotifications;
    }

    public int getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications(int totalNotifications) {
        this.totalNotifications = totalNotifications;
    }

    public int getArticleNotifications() {
        return articleNotifications;
    }

    public void setArticleNotifications(int articleNotifications) {
        this.articleNotifications = articleNotifications;
    }
}
