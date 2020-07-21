package edu.odu.cs411yellow.gameeyebackend.models.games;

/**
 * Holds the source URLs for a game.
 * These are important URLs related to the game, and my be used for web scraping.
 */
public class SourceUrls {
    private String gameUrl;
    private String steamUrl;
    private String subRedditUrl;
    private String twitterUrl;

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

    public void setGameUrl(String newGameUrl) {
        this.gameUrl = newGameUrl;
    }

    public String getSteamUrl() {
        return this.steamUrl;
    }

    public void setSteamUrl(String newSteamUrl) {
        this.steamUrl = newSteamUrl;
    }

    public String getSubRedditUrl() {
        return this.subRedditUrl;
    }

    public void setSubRedditUrl(String newSubRedditUrl) {
        this.subRedditUrl = newSubRedditUrl;
    }

    public String getTwitterUrl() {
        return this.twitterUrl;
    }

    public void setTwitterUrl(String newTwitterUrl) {
        this.twitterUrl = newTwitterUrl;
    }
}
