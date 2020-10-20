package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses;

public class WatchedGameShortResponse {
    private String id;
    private String title;

    public WatchedGameShortResponse() {
        this.id = "";
        this.title = "";
    }

    public WatchedGameShortResponse(String id, String title) {
        this.id = id;
        this.title = title;
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
}
