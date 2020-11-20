package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.notifications.ResourceNotifications;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.List;
import java.util.Objects;

public class WatchedGame {
    protected String gameId;

    protected ResourceNotifications resourceNotifications;

    @PersistenceConstructor
    public WatchedGame(String gameId,
                       ResourceNotifications resourceNotifications) {
        this.gameId = gameId;
        this.resourceNotifications = resourceNotifications;
    }

    public WatchedGame(WatchedGame other) {
        this.gameId = other.gameId;
        this.resourceNotifications = other.resourceNotifications;
    }

    public WatchedGame(Game game) {
        this.gameId = game.getId();
    }

    public WatchedGame() {
        this.gameId = "";
        this.resourceNotifications = new ResourceNotifications();
    }

    public String getGameId() {
        return this.gameId;
    }

    public ResourceNotifications getResourceNotifications() {
        return this.resourceNotifications;
    }

    public void setResourceNotifications(ResourceNotifications resourceNotifications) {
        this.resourceNotifications = resourceNotifications;
    }

    public void addArticlesToResources(List<String> articleIds) {
        resourceNotifications.getArticleNotifications().setArticleIds(articleIds);
    }

    public void removeArticlesFromResources(List<String> articleIds) {
        resourceNotifications.getArticleNotifications().removeArticles(articleIds);
    }

    public void removeAllArticlesFromResources() {
        resourceNotifications.getArticleNotifications().removeAllArticles();
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
