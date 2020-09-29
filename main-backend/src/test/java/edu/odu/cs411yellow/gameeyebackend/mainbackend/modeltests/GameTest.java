package edu.odu.cs411yellow.gameeyebackend.mainbackend.modeltests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.bson.types.Binary;
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


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class GameTest {

    @Autowired
    private GameRepository gameRepository;

    Game insertedGame;

    @BeforeEach
    public void insertGameIntoGameEyeTest () {
        String gameId = "5e98bf94a3464d35b824d04f";
        String gameTitle = "Doom Eternal";
        List<String> platforms = new ArrayList<>(Arrays.asList("Stadia", "Xbox One", "Nintendo Switch",
                "PS4", "Mobile"));
        String status = "Released";
        Date gameLastUpdated = new Date(2020, 9, 23);
        List<String> genres = new ArrayList<>(Arrays.asList("first-person shooter"));

        // Declare sourceUrls
        String publisherUrl = "https://bethesda.net/en/game/doom";
        String steamUrl = "https://store.steampowered.com/app/782330/DOOM_Eternal/";
        String subRedditUrl = "https://www.reddit.com/r/Doometernal/";
        String twitterUrl = "https://twitter.com/DOOM?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";

        SourceUrls sourceUrls = new SourceUrls(publisherUrl, steamUrl,
                subRedditUrl, twitterUrl);

        // Declare gameImage
        String gameImageId = "5ea1c2b677dabd049ce92784";
        String imageTitle = "gameplay";
        String imageRefId = "5ea10b6d34019c1d1c818c03";
        Date imageLastUpdated = new Date(2020, 4, 21);

        ImageResource imageResource = new ImageResource(gameImageId, imageTitle,
                imageRefId, imageLastUpdated);

        List<ImageResource> images = new ArrayList<>(Arrays.asList(imageResource));

        // Declare newsWebsite
        String newsWebsiteId = "5e9fbb092937d83b902ec992";
        String newsWebsiteName = "IGN";
        Binary newsWebsiteLogo = new Binary(new byte[1]);
        String newsWebsiteUrl = "https://www.ign.com/";
        String newsWebsiteRssFeedUrl = "https://corp.ign.com/feeds";
        Date newsWebsiteLastUpdated = new Date(2020, 4, 21);

        NewsWebsite newsWebsite = new NewsWebsite(newsWebsiteId, newsWebsiteName,
                newsWebsiteLogo, newsWebsiteUrl,
                newsWebsiteRssFeedUrl, newsWebsiteLastUpdated);

        // Declare image
        String imageId = "5ea108ea34019c1d1c818c02";
        String type = "thumbnail";
        Binary imageData = new Binary(new byte[1]);

        Image thumbnail = new Image(imageId, type, imageData);

        // Declare article object
        String articleId = "5ea1c2e777dabd049ce92788";
        String articleTitle = "Doom Eternal Single-Player Review";
        String articleUrl = "https://www.ign.com/articles/doom-eternal-single-player-review";

        String articleSnippet = "Doom Eternal not only retains the wild, high-speed, to-the-brink-of-" +
                "death-and-back-again ebb and flow of combat that its 2016 predecessor " +
                "excelled at, it tweaks the formula to introduce more strategy, replayability," +
                " and ultimately...";
        Date publicationDate = new Date(2020, 4, 21);
        Date articleLastUpdated = new Date(2020, 8, 27);
        int impactScore = 1;

        Article article = new Article(articleId, articleTitle, articleUrl, newsWebsite,
                thumbnail, articleSnippet, publicationDate,
                articleLastUpdated, impactScore);

        List<Article> articles = new ArrayList<>(Arrays.asList(article));

        Resources resources = new Resources(images, articles);

        insertedGame = new Game(gameId, gameTitle, platforms, status, gameLastUpdated, genres,
                                sourceUrls, resources);

        gameRepository.save(insertedGame);
    }

    @AfterEach
    public void deleteGameFromGameEyeTest () {
        String gameId = insertedGame.getId();

        if (gameRepository.existsById(gameId))
            gameRepository.deleteById(gameId);

        Assert.assertFalse(gameRepository.existsById(gameId));
    }

    @Test
    public void testFindArticles () {

        String gameId = insertedGame.getId();
        Game foundGame = gameRepository.findGameById(gameId);

        Resources actualResources = insertedGame.getResources();


        String articleId = "5ea1c2e777dabd049ce92788";

        List<String> articleIds = new ArrayList<>(Arrays.asList(articleId));

        List<Article> actualArticles = actualResources.getArticles();
        List<Article> foundArticles = foundGame.findArticles(articleIds);

        for (int i = 0; i < foundArticles.size(); i++) {
            Article foundArticle = foundArticles.get(i);
            Article actualArticle = actualArticles.get(i);

            assert(foundArticle.equals(actualArticle));
        }
    }
}
