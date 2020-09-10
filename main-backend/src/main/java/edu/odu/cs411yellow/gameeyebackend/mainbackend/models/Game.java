package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Class representing a document in the "Games" database collection.
 */
@Document("games")
public class Game {
    @Id
    private String id;

    @Indexed(unique = true)
    private String title;

    private List<String> platforms;

    private String status;

    private Date lastUpdated;

    private List<String> genres;

    private List<SourceUrls> sourceUrls;

    // TODO add resources fields
    private List<Article> articles;

    // TODO implement getters and setters
}
