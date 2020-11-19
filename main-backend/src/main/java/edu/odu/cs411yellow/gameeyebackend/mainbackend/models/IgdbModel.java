package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
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
        public long lastUpdatedInSeconds;
        @JsonProperty("genres")
        public List<GenreResponse> genres;
        @JsonProperty("websites")
        public List<WebsiteResponse> sourceUrls;
        @JsonProperty("first_release_date")
        public long firstReleaseDateInSeconds;

        public GameResponse() {
            this.igdbId = "";
            this.title = "";
            this.platforms = new ArrayList<>();
            this.lastUpdatedInSeconds = 0;
            this.genres = new ArrayList<>();
            this.sourceUrls = new ArrayList<>();
            this.firstReleaseDateInSeconds = 0;
        }

        public GameResponse(String igdbId, String title, List<PlatformResponse> platforms,
                            List<GenreResponse> genres, long lastUpdatedInSeconds,
                            List<WebsiteResponse> sourceUrls, long firstReleaseDateInSeconds) {
            this.igdbId = igdbId;
            this.title = title;
            this.platforms = platforms;
            this.lastUpdatedInSeconds = lastUpdatedInSeconds;
            this.genres = genres;
            this.sourceUrls = sourceUrls;
            this.firstReleaseDateInSeconds = firstReleaseDateInSeconds;
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

                if (responseCode.equals(SourceUrlCategory.official.toString())) {
                    sourceUrls.setPublisherUrl(websiteResponse.url);
                } else if (responseCode.equals(SourceUrlCategory.steam.toString())) {
                    sourceUrls.setSteamUrl(websiteResponse.url);
                } else if (responseCode.equals(SourceUrlCategory.subReddit.toString())) {
                    sourceUrls.setSubRedditUrl(websiteResponse.url);
                } else if (responseCode.equals(SourceUrlCategory.twitter.toString())) {
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

    public static class CoverResponse {
        @JsonProperty("id")
        public String logoId;
        @JsonProperty("url")
        public String logoUrl;
        @JsonProperty("game")
        public String gameId;

        public CoverResponse() {
            this.logoId = "";
            this.logoUrl = "";
            this.gameId = "";
        }

        public CoverResponse(String id, String logoUrl, String gameId) {
            this.logoId = id;
            this.logoUrl = logoUrl;
            this.gameId = gameId;
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

        @Override
        public String toString() {
            return this.categoryCode;
        }
    }

    public static class IdResponse {
        @JsonProperty("game")
        private String gameId;
        @JsonIgnore
        @JsonProperty("id")
        private String releaseId;

        @JsonProperty("date")
        private long releaseDate;

        public IdResponse(String gameId, long releaseDate) {
            this.gameId = gameId;
            this.releaseDate = releaseDate;
        }

        public String getGameId() {
            return gameId;
        }

        public void setId(String id) {
            this.gameId = gameId;
        }

        public String getReleaseId() {
            return releaseId;
        }

        public void setReleaseId(String releaseId) {
            this.releaseId = releaseId;
        }

        public long getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(long releaseDate) {
            this.releaseDate = releaseDate;
        }
    }
}
