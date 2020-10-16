package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ImageRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class GameRepositoryTest {

    @Autowired
    private GameRepository games;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private NewsWebsiteRepository news;

    @Autowired
    private IgdbService igdbService;

    Game insertedGame;

    @BeforeEach
    public void insertGameIntoGameEyeTest() throws JsonProcessingException {
        // Declare resource images
        String gameImageId = "5ea1c2b677dabd049ce92784";
        String imageTitle = "gameplay";
        String imageRefId = "5ea10b6d34019c1d1c818c03";
        Date imageLastUpdated = new Date(120, 4, 21);

        ImageResource imageResource = new ImageResource(gameImageId, imageTitle,
                                                        imageRefId, imageLastUpdated);

        List<ImageResource> images = new ArrayList<>(Arrays.asList(imageResource));

        // Retrieve newsWebsite from newsWebsites collection
        NewsWebsite newsWebsite = news.findByName("IGN");

        // Retrieve thumbnail from images collection
        String imageId = "5f7aa192685dad531c57d54d";
        Image thumbnail = imageRepository.findImageById(imageId);

        // Declare article object
        String articleId = "5ea1c2e777dabd049ce92788";
        String articleTitle = "Doom Eternal Single-Player Review";
        String articleUrl = "https://www.ign.com/articles/doom-eternal-single-player-review";

        String articleSnippet = "Doom Eternal not only retains the wild, high-speed, to-the-brink-of-" +
                "death-and-back-again ebb and flow of combat that its 2016 predecessor " +
                "excelled at, it tweaks the formula to introduce more strategy, replayability," +
                " and ultimately...";
        Date publicationDate = new Date(120, 4, 21);
        Date articleLastUpdated = new Date(120, 8, 27);
        int impactScore = 1;

        Article article = new Article(articleId, articleTitle, articleUrl, newsWebsite,
                thumbnail, articleSnippet, publicationDate,
                articleLastUpdated, impactScore);

        List<Article> articles = new ArrayList<>(Arrays.asList(article));

        Resources resources = new Resources(images, articles);

        int doomEternalId = 103298;
        insertedGame = igdbService.getGameById(doomEternalId);
        insertedGame.setResources(resources);

        games.save(insertedGame);
    }

    @AfterEach
    public void deleteInsertedGame() {
        String gameTitle = insertedGame.getTitle();

        if (games.existsByTitle(gameTitle)) {
            games.deleteByTitle(gameTitle);
        }

        assertThat(games.existsByTitle(gameTitle), is(false));
    }

    @Test
    public void findGameByTitle() {
        String gameTitle = insertedGame.getTitle();

        Game foundGame = games.findByTitle(gameTitle);

        // Test all but the id, which is null in insertedGame.
        assertThat(insertedGame.getIgdbId(), equalTo(foundGame.getIgdbId()));
        assertThat(insertedGame.getTitle(), equalTo(foundGame.getTitle()));
        assertThat(insertedGame.getPlatforms(), equalTo(foundGame.getPlatforms()));
        assertThat(insertedGame.getStatus(), equalTo(foundGame.getStatus()));
        assertThat(insertedGame.getLastUpdated(), equalTo(foundGame.getLastUpdated()));
        assertThat(insertedGame.getGenres(), equalTo(foundGame.getGenres()));
        assertThat(insertedGame.getSourceUrls(), equalTo(foundGame.getSourceUrls()));
        assertThat(insertedGame.getResources(), equalTo(foundGame.getResources()));
        assertThat(insertedGame.getWatchers(), equalTo(foundGame.getWatchers()));
    }
}
