package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import java.util.List;

public class GameResponse {
    public String id;
    public List<String> genres;
    public String name;
    public int updated_at;


    // Stores reference Ids for websites
    public List<String> websites;


    public GameResponse () {

    }

    public GameResponse (String id, List<String> genres, String name, int updated_at, List<String> websites) {
        this.id = id;
        this.name = name;
        this.updated_at = updated_at;
        this.genres = genres;
        this.websites = websites;
    }
}