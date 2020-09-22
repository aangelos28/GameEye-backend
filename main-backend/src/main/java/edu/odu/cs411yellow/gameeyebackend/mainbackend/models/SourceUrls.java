package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.springframework.data.annotation.PersistenceConstructor;

/**
 * Holds the source URLs for a game.
 * These are important URLs related to the game, and my be used for web scraping.
 */
public class SourceUrls {
    /**
     * URL to a publisher website (i.e. official game website).
     */
    private String publisherUrl;

    /**
     * URL to the Steam store page of a game.
     */
    private String steamUrl;

    /**
     * URL to the subreddit of a game.
     */
    private String subRedditUrl;

    /**
     * URL to the official Twitter feed of a game.
     */
    private String twitterUrl;

    @PersistenceConstructor
    public SourceUrls(String publisherUrl, String steamUrl, String subRedditUrl,
                      String twitterUrl) {
        this.publisherUrl = publisherUrl;
        this.steamUrl = steamUrl;
        this.subRedditUrl = subRedditUrl;
        this.twitterUrl = twitterUrl;
    }

    public SourceUrls() {
        this.publisherUrl = "";
        this.steamUrl = "";
        this.subRedditUrl = "";
        this.twitterUrl = "";
    }

    public String getPublisherUrl() {
        return this.publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
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