package edu.odu.cs411yellow.gameeyebackend.models.games;

import java.util.List;

public class Game {
    private String title;
    private List<String> platforms;
    private String status;
    // TODO add lastUpdated date field
    private List<String> genres;
    private List<SourceUrls> sourceUrls;
}
