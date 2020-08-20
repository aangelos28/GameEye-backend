package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Holds the source URLs for a game.
 * These are important URLs related to the game, and my be used for web scraping.
 */
public class SourceUrls {
    private String gameUrl;

    private String steamUrl;

    private String subRedditUrl;

    private String twitterUrl;

    @PersistenceConstructor
    public SourceUrls(String gameUrl, String steamUrl, String subRedditUrl,
                      String twitterUrl) {
        this.gameUrl = gameUrl;
        this.steamUrl = steamUrl;
        this.subRedditUrl = subRedditUrl;
        this.twitterUrl = twitterUrl;
    }

    ///////////////////////////////////////////////
    // Getters/Setters
    ///////////////////////////////////////////////
    public String getGameUrl() {
        return this.gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getSteamUrl() {
        return this.steamUrl;
    }

    public void setSteamUrl(String steamUrl) {
        this.steamUrl = steamUrl;
    }

    public String getSubRedditUrl() {
        return this.subRedditUrl;
    }

    public void setSubRedditUrl(String subRedditUrl) {
        this.subRedditUrl = subRedditUrl;
    }

    public String getTwitterUrl() {
        return this.twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }
}
