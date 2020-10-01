package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import java.util.Date;
import java.util.List;

public class IgdbModel {
    public class GameResponse {
        public String id;
        public List<String> genres;
        public String name;
        public Date updated_at;


        // Stores reference Ids for websites
        public List<String> websites;

        public GameResponse (String id, List<String> genres, String name, Integer updated_at, List<String> websites) {
            this.id = id;
            this.name = name;
            this.updated_at = new Date(updated_at);
            this.genres = genres;
            this.websites = websites;
        }
    }
}
