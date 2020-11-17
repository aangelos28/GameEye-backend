package edu.odu.cs411yellow.gameeyebackend.mainbackend.modeltests;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ImageRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class GameTest {

    @Autowired
    private GameRepository games;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private NewsWebsiteRepository news;

    @Autowired
    private IgdbService igdbService;

    Game insertedGame;
    String articleId = "5ea1c2e777dabd049ce92788";

    @BeforeEach
    public void insertGameTest() {
        // Declare resource images
        String gameImageId = "5ea1c2b677dabd049ce92784";
        String imageTitle = "gameplay";
        String imageRefId = "5ea10b6d34019c1d1c818c03";
        Date imageLastUpdated = new Date(120, 4, 21);

        ImageResource imageResource = new ImageResource(gameImageId, imageTitle,
                imageRefId, imageLastUpdated);

        List<ImageResource> images = new ArrayList<>(Arrays.asList(imageResource));

        String newsWebsiteName = "IGN";
        String thumbnailId = "GameTest - ThumbnailId1";

        // Declare article object
        String articleTitle = "Doom Eternal Single-Player Review";
        String articleUrl = "https://www.ign.com/articles/doom-eternal-single-player-review";

        String articleSnippet = "Doom Eternal not only retains the wild, high-speed, to-the-brink-of-" +
                "death-and-back-again ebb and flow of combat that its 2016 predecessor " +
                "excelled at, it tweaks the formula to introduce more strategy, replayability," +
                " and ultimately...";
        Date publicationDate = new Date(120, 4, 21);
        Date articleLastUpdated = new Date(120, 8, 27);
        boolean important = true;

        Article article = new Article(articleId, articleTitle, articleUrl, newsWebsiteName,
                thumbnailId, articleSnippet, publicationDate,
                articleLastUpdated, important);

        List<Article> articles = new ArrayList<>(Arrays.asList(article));

        Resources resources = new Resources(images, articles);

        int doomEternalId = 103298;
        insertedGame = igdbService.getGameById(doomEternalId);
        insertedGame.setResources(resources);

        games.save(insertedGame);
    }

    @AfterEach
    public void deleteGameTest() {
        String gameTitle = insertedGame.getTitle();

        if (games.existsByTitle(gameTitle)) {
            games.deleteByTitle(gameTitle);
        }

        Assert.assertFalse(games.existsByTitle(gameTitle));
    }

    @Test
    public void testFindArticles() {
        String gameTitle = insertedGame.getTitle();
        Game foundGame = games.findGameByTitle(gameTitle);

        Resources actualResources = insertedGame.getResources();

        List<String> articleIds = new ArrayList<>(Arrays.asList(articleId));

        List<Article> actualArticles = actualResources.getArticles();
        List<Article> foundArticles = foundGame.findArticles(articleIds);

        for (int i = 0; i < foundArticles.size(); i++) {
            Article foundArticle = foundArticles.get(i);
            Article actualArticle = actualArticles.get(i);

            assert(foundArticle.equals(actualArticle));
        }
    }

    @Test
    public void testGameConstructorFromGameResponse () {
        int igdbId = 100;
        IgdbModel.GameResponse gameResponse = igdbService.getGameResponseById(igdbId);
        Game convertedGame = new Game(gameResponse);
        Game originalGame = igdbService.getGameById(igdbId);

        assertThat(originalGame, equalTo(convertedGame));
    }
}
