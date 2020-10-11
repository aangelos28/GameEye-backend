package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IgdbModel {

    public static class GameResponse {
        @JsonProperty("id")
        public String igdbId;
        @JsonProperty("name")
        public String title;
        @JsonProperty("platforms")
        public List<PlatformResponse> platforms;
        @JsonProperty("updated_at")
        public int lastUpdatedInSeconds;
        @JsonProperty("genres")
        public List<GenreResponse> genres;
        @JsonProperty("websites")
        public List<WebsiteResponse> sourceUrls;

        public GameResponse() {
            this.igdbId = "";
            this.title = "";
            this.platforms = new ArrayList<>();
            this.lastUpdatedInSeconds = 0;
            this.genres = new ArrayList<>();
            this.sourceUrls = new ArrayList<>();
        }

        public GameResponse(String igdbId, String title, List<PlatformResponse> platforms,
                            List<GenreResponse> genres, int lastUpdatedInSeconds,
                            List<WebsiteResponse> sourceUrls) {
            this.igdbId = igdbId;
            this.title = title;
            this.platforms = platforms;
            this.lastUpdatedInSeconds = lastUpdatedInSeconds;
            this.genres = genres;
            this.sourceUrls = sourceUrls;
        }

        public List<String> getGenres() {
            List<String> genres = new ArrayList<>();

            for (GenreResponse genreResponse: this.genres) {
                genres.add(genreResponse.name);
            }

            return genres;
        }

        public SourceUrls getSourceUrls() {
            SourceUrls sourceUrls = new SourceUrls();

            for (WebsiteResponse websiteResponse: this.sourceUrls) {
                String responseCode = websiteResponse.category;
                String officialCode = SourceUrlCategory.official.toString();
                String steamCode = SourceUrlCategory.steam.toString();
                String subRedditCode = SourceUrlCategory.subReddit.toString();
                String twitterCode = SourceUrlCategory.twitter.toString();

                if (officialCode.equals(responseCode)) {
                    sourceUrls.setPublisherUrl(websiteResponse.url);
                } else if (steamCode.equals(responseCode)) {
                    sourceUrls.setSteamUrl(websiteResponse.url);
                } else if (subRedditCode.equals(responseCode)) {
                    sourceUrls.setSubRedditUrl(websiteResponse.url);
                } else if (twitterCode.equals(responseCode)) {
                    sourceUrls.setTwitterUrl(websiteResponse.url);
                }
            }

            return sourceUrls;
        }

        public List<String> getPlatforms() {
            List<String> platforms = new ArrayList<>();

            for (PlatformResponse platformResponse: this.platforms) {
                platforms.add(platformResponse.name);
            }

            return platforms;
        }

        public Game toGame() {
            String id = "";
            String igdbId = this.igdbId;
            String title = this.title;
            List<String> platforms = this.getPlatforms();
            String status = "";

            // Convert UNIX epoch timestamp from IGDB to year, month, day format
            Date lastUpdated = new java.util.Date((long)this.lastUpdatedInSeconds * 1000);
            List<String> genres = this.getGenres();
            SourceUrls sourceUrls = this.getSourceUrls();
            Resources resources = new Resources();
            int watchers = 0;

            Game game = new Game(id, igdbId, title, platforms, status, lastUpdated,
                                 genres, sourceUrls, resources, watchers);

            return game;
        }
    }

    public static class GenreResponse {
        public String id;
        public String name;

        public GenreResponse() {
            this.id = "";
            this.name = "";
        }

        public GenreResponse(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class WebsiteResponse {
        public String id;
        public String url;
        public String category;

        public WebsiteResponse() {
            this.id = "";
            this.url = "";
            this.category = "";
        }

        public WebsiteResponse(String id, String url, String category) {
            this.id = id;
            this.url = url;
            this.category = category;
        }
    }

    public static class PlatformResponse {
        public String id;
        public String name;

        public PlatformResponse() {
            this.id = "";
            this.name = "";
        }

        public PlatformResponse(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class AuthResponse {
        @JsonProperty("access_token")
        public String accessToken;
        @JsonProperty("expires_in")
        public int expiresIn;
        @JsonProperty("token_type")
        public String tokenType;

        public AuthResponse() {
            this.accessToken = "";
            this.expiresIn = 0;
            this.tokenType = "";
        }

        public AuthResponse(String accessToken, int expiresIn, String tokenType) {
            this.accessToken = accessToken;
            this.expiresIn = expiresIn;
            this.tokenType = tokenType;
        }
    }

    public static class CompanyResponse {
        public String id;
        public String name;

        public CompanyResponse() {
            this.id = "";
            this.name = "";
        }

        public CompanyResponse(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /**
     * Website category enums from IGDB API v4 docs.
     */
    public enum SourceUrlCategory {
        official("1"),
        steam("13"),
        subReddit("14"),
        twitter("5");

        private final String categoryCode;

        SourceUrlCategory(String categoryCode) {
            this.categoryCode = categoryCode;
        }
    }
}
