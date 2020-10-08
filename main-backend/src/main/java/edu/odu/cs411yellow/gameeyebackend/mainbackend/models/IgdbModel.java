package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IgdbModel {

    public static class GameResponse {
        public String id;
        public String igdbId;
        public String name;
        @JsonProperty("platforms")
        public List<PlatformResponse> platformResponses;
        @JsonProperty("updated_at")
        public int lastUpdated;
        @JsonProperty("genres")
        public List<GenreResponse> genreResponses;
        @JsonProperty("websites")
        public List<WebsiteResponse> websiteResponses;

        public GameResponse() {
            this.id = "";
            this.igdbId = "";
            this.name = "";
            this.platformResponses = new ArrayList<>();
            this.lastUpdated = 0;
            this.genreResponses = new ArrayList<>();
            this.websiteResponses = new ArrayList<>();
        }

        public GameResponse(String id, String igdbId, String name, List<PlatformResponse> platformResponses,
                            List<GenreResponse> genreResponses, int lastUpdated,
                            List<WebsiteResponse> websiteResponses) {
            this.id = id;
            this.igdbId = igdbId;
            this.name = name;
            this.platformResponses = platformResponses;
            this.lastUpdated = lastUpdated;
            this.genreResponses = genreResponses;
            this.websiteResponses = websiteResponses;
        }

        public List<String> getGenresFromGenreResponses() {
            List<String> genres = new ArrayList<>();

            for(GenreResponse genreResponse: this.genreResponses) {
                genres.add(genreResponse.name);

            }

            return genres;
        }

        public SourceUrls getSourceUrlsFromWebsiteResponses() {
            SourceUrls sourceUrls = new SourceUrls();

            String igdbOfficialEnumValue = "1";
            String igdbSteamEnumValue = "13";
            String igdbRedditEnumValue = "14";
            String igdbTwitterEnumValue = "5";

            for(WebsiteResponse websiteResponse: websiteResponses) {
                if(igdbOfficialEnumValue.equals(websiteResponse.category)) {
                    sourceUrls.setPublisherUrl(websiteResponse.url);
                } else if (igdbSteamEnumValue.equals(websiteResponse.category)) {
                    sourceUrls.setSteamUrl(websiteResponse.url);
                } else if (igdbRedditEnumValue.equals(websiteResponse.category)) {
                    sourceUrls.setSubRedditUrl(websiteResponse.url);
                } else if (igdbTwitterEnumValue.equals(websiteResponse.category)) {
                    sourceUrls.setTwitterUrl(websiteResponse.url);
                }
            }

            return sourceUrls;
        }

        public List<String> getPlatformsFromPlatformResponses() {
            List<String> platforms = new ArrayList<>();

            for(PlatformResponse platformResponse: this.platformResponses) {
                platforms.add(platformResponse.name);

            }

            return platforms;
        }

        public Game toGame() {
            String id = "";
            String igdbId = this.igdbId;
            String title = this.name;
            List<String> platforms = this.getPlatformsFromPlatformResponses();
            String status = "";

            // Convert UNIX epoch timestamp from IGDB to year, month, day format
            Date lastUpdated = new java.util.Date((long)this.lastUpdated*1000);
            List<String> genres = this.getGenresFromGenreResponses();
            SourceUrls sourceUrls = this.getSourceUrlsFromWebsiteResponses();
            Resources resources = new Resources();

            Game game = new Game(id, igdbId, title, platforms, status, lastUpdated, genres, sourceUrls, resources);

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
}
