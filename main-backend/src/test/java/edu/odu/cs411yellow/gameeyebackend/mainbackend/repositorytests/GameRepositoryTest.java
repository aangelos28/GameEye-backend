package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

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

import static org.hamcrest.CoreMatchers.is;
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
    public void insertGameIntoGameEyeTest() {
        // Declare resource images
        String gameImageId = "5ea1c2b677dabd049ce92784";
        String imageTitle = "gameplay";
        String imageRefId = "5ea10b6d34019c1d1c818c03";
        Date imageLastUpdated = new Date(120, 4, 21);

        ImageResource imageResource = new ImageResource(gameImageId, imageTitle,
                                                        imageRefId, imageLastUpdated);

        List<ImageResource> images = new ArrayList<>(Arrays.asList(imageResource));

        String newsWebsiteName = "IGN";
        String thumbnailId = "GameRepositoryTest - ThumbnailId1";

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
        boolean important = true;

        Article article = new Article(articleId, articleTitle, articleUrl, newsWebsiteName,
                thumbnailId, articleSnippet, publicationDate,
                articleLastUpdated, important);

        List<Article> articles = new ArrayList<>(Arrays.asList(article));

        Resources resources = new Resources(images, articles);

        int doomEternalId = 103298;
        insertedGame = igdbService.retrieveGameById(doomEternalId);
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
        assertThat(insertedGame.getReleaseDate(), equalTo(foundGame.getReleaseDate()));
        assertThat(insertedGame.getLastUpdated(), equalTo(foundGame.getLastUpdated()));
        assertThat(insertedGame.getGenres(), equalTo(foundGame.getGenres()));
        assertThat(insertedGame.getSourceUrls(), equalTo(foundGame.getSourceUrls()));
        assertThat(insertedGame.getResources(), equalTo(foundGame.getResources()));
        assertThat(insertedGame.getWatchers(), equalTo(foundGame.getWatchers()));
    }

    @Test
    public void testDeleteArticlesFromGameById() {
        String articleTitle1 = "GameRepositoryTest - Article1";
        String articleTitle2 = "GameRepositoryTest - Article2";
        String articleTitle3 = "GameRepositoryTest - Article3";

        Article article1 = new Article();
        article1.setTitle(articleTitle1);

        Article article2 = new Article();
        article2.setTitle(articleTitle2);

        Article article3 = new Article();
        article3.setTitle(articleTitle3);

        List<Article> articles = new ArrayList<>(Arrays.asList(article1, article2, article3));

        Game preGame1 = new Game();
        preGame1.setId("GameRepositoryTest - Id1");
        preGame1.setTitle("GameRepositoryTest - Title1");
        preGame1.setIgdbId("1");
        preGame1.getResources().setArticles(articles);

        Game preGame2 = new Game();
        preGame2.setId("GameRepositoryTest - Id2");
        preGame2.setTitle("GameRepositoryTest - Title2");
        preGame2.setIgdbId("2");
        preGame2.getResources().setArticles(articles);

        Game preGame3 = new Game();
        preGame3.setId("GameRepositoryTest - Id3");
        preGame3.setTitle("GameRepositoryTest - Title3");
        preGame3.setIgdbId("3");
        preGame3.getResources().setArticles(articles);

        // Insert all 3 games.
        games.save(preGame1);
        games.save(preGame2);
        games.save(preGame3);

        // Delete articles from preGame1 only.
        games.deleteArticlesFromGameById(preGame1.getId());

        Game postGame1 = games.findGameById(preGame1.getId());
        Game postGame2 = games.findGameById(preGame2.getId());
        Game postGame3 = games.findGameById(preGame3.getId());

        assertThat(postGame1.getResources().getArticles().size(), is(0));
        assertThat(postGame2.getResources().getArticles().size(), is(preGame2.getResources().getArticles().size()));
        assertThat(postGame3.getResources().getArticles().size(), is(preGame3.getResources().getArticles().size()));

        games.deleteAll();
    }

    @Test
    public void testDeleteArticlesFromGameByTitle() {
        String articleTitle1 = "GameRepositoryTest - Article1";
        String articleTitle2 = "GameRepositoryTest - Article2";
        String articleTitle3 = "GameRepositoryTest - Article3";

        Article article1 = new Article();
        article1.setTitle(articleTitle1);

        Article article2 = new Article();
        article2.setTitle(articleTitle2);

        Article article3 = new Article();
        article3.setTitle(articleTitle3);

        List<Article> articles = new ArrayList<>(Arrays.asList(article1, article2, article3));

        Game preGame1 = new Game();
        preGame1.setId("GameRepositoryTest - Id1");
        preGame1.setTitle("GameRepositoryTest - Title1");
        preGame1.setIgdbId("1");
        preGame1.getResources().setArticles(articles);

        Game preGame2 = new Game();
        preGame2.setId("GameRepositoryTest - Id2");
        preGame2.setTitle("GameRepositoryTest - Title2");
        preGame2.setIgdbId("2");
        preGame2.getResources().setArticles(articles);

        Game preGame3 = new Game();
        preGame3.setId("GameRepositoryTest - Id3");
        preGame3.setTitle("GameRepositoryTest - Title3");
        preGame3.setIgdbId("3");
        preGame3.getResources().setArticles(articles);

        // Insert all 3 games.
        preGame1 = games.save(preGame1);
        preGame2 = games.save(preGame2);
        preGame3 = games.save(preGame3);

        // Delete articles from preGame1 only.
        games.deleteArticlesFromGameByTitle(preGame1.getTitle());

        Game postGame1 = games.findGameById(preGame1.getId());
        Game postGame2 = games.findGameById(preGame2.getId());
        Game postGame3 = games.findGameById(preGame3.getId());

        assertThat(postGame1.getResources().getArticles().size(), is(0));
        assertThat(postGame2.getResources().getArticles().size(), is(preGame2.getResources().getArticles().size()));
        assertThat(postGame3.getResources().getArticles().size(), is(preGame3.getResources().getArticles().size()));

        games.deleteAll();
    }

    @Test
    public void testDeleteAllArticlesInAllGames() {
        String articleTitle1 = "GameRepositoryTest - Article1";
        String articleTitle2 = "GameRepositoryTest - Article2";
        String articleTitle3 = "GameRepositoryTest - Article3";

        Article article1 = new Article();
        article1.setTitle(articleTitle1);

        Article article2 = new Article();
        article2.setTitle(articleTitle2);

        Article article3 = new Article();
        article3.setTitle(articleTitle3);

        List<Article> articles = new ArrayList<>(Arrays.asList(article1, article2, article3));

        Game preGame1 = new Game();
        preGame1.setTitle("GameRepositoryTest - Game1");
        preGame1.setIgdbId("1");
        preGame1.getResources().setArticles(articles);

        Game preGame2 = new Game();
        preGame2.setTitle("GameRepositoryTest - Game2");
        preGame2.setIgdbId("2");
        preGame2.getResources().setArticles(articles);

        Game preGame3 = new Game();
        preGame3.setTitle("GameRepositoryTest - Game3");
        preGame3.setIgdbId("3");
        preGame3.getResources().setArticles(articles);

        // Insert all 3 games.
        preGame1 = games.save(preGame1);
        preGame2 = games.save(preGame2);
        preGame3 = games.save(preGame3);

        // Delete articles from preGame1 only.
        games.deleteArticlesFromAllGames();

        Game postGame1 = games.findGameByTitle(preGame1.getTitle());
        Game postGame2 = games.findGameByTitle(preGame2.getTitle());
        Game postGame3 = games.findGameByTitle(preGame3.getTitle());

        assertThat(postGame1.getResources().getArticles().size(), is(0));
        assertThat(postGame2.getResources().getArticles().size(), is(0));
        assertThat(postGame3.getResources().getArticles().size(), is(0));

        games.deleteAll();
    }
}
