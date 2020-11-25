package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses;

public class WatchedGameShortResponse {
    private String id;
    private String title;
    private String releaseDate;

    public WatchedGameShortResponse() {
        this.id = "";
        this.title = "";
        this.releaseDate = "";
    }

    public WatchedGameShortResponse(String id, String title, String releaseDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
