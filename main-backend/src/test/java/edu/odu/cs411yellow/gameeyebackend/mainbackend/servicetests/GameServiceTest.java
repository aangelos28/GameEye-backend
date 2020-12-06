package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class GameServiceTest {
    @Autowired
    private GameService gameService;

    @Autowired
    private IgdbService igdbService;

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void testGetLogoUrl() {
        final String game1Title = "Vampire: The Masquerade - Bloodlines - GameServiceTest";
        final String igdbId1 = "11";
        final String logoUrl1 = "//images.igdb.com/igdb/image/upload/t_thumb/co1nkj.jpg";

        Game game1 = new Game();
        game1.setTitle(game1Title);
        game1.setIgdbId(igdbId1);
        game1.setLogoUrl(logoUrl1);

        gameRepository.save(game1);
        game1 = gameRepository.findByIgdbId(igdbId1);

        String retrievedLogoUrl = gameService.getLogoUrl(game1.getId());

        // Check that logoUrls match
        assertThat(retrievedLogoUrl, is(game1.getLogoUrl()));

        // Check that logoUrls contain ".jpg"
        assertThat(retrievedLogoUrl, containsString(".jpg"));

        gameRepository.deleteAll();
    }

    @Test
    public void testFindArticles() {
        // Declare resource images
        String gameImageId = "5ea1c2b677dabd049ce92784";
        String imageTitle = "gameplay";
        String imageRefId = "5ea10b6d34019c1d1c818c03";
        Date imageLastUpdated = new Date(120, 4, 21);

        ImageResource imageResource = new ImageResource(gameImageId, imageTitle,
                imageRefId, imageLastUpdated);

        List<ImageResource> images = new ArrayList<>(Arrays.asList(imageResource));

        String articleId = "5ea1c2e777dabd049ce92788";

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
        Game newGame = igdbService.retrieveGameById(doomEternalId);
        newGame.setResources(resources);

        gameRepository.insert(newGame);
        Game insertedGame = gameRepository.findGameByTitle(newGame.getTitle());

        List<Article> retrievedArticles = gameService.findArticles(insertedGame.getId());
        assertThat(retrievedArticles, is(insertedGame.getResources().getArticles()));
        retrievedArticles.forEach((article1) -> System.out.println(article1.getTitle()));

        gameRepository.delete(insertedGame);
    }

    @Test
    public void testTopGames() {
        int minId = 1000;
        int maxId = 1010;
        int limit = 10;

        List<Game> testGames = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);

        // Set top n games
        int totalTopGames = 5;
        int watchers = 100;

        for (int i = 0; i < totalTopGames + 1; i++) {
            testGames.get(i).setWatchers((watchers * (totalTopGames - i)));
        }

        gameRepository.saveAll(testGames);

        int maxResults = 5;
        List<Game> foundGames = gameService.getTopGames(maxResults);

        for (int i = 0; i < foundGames.size(); i++) {
            assertThat(foundGames.get(i).getTitle(), equalTo(testGames.get(i).getTitle()));
            assertThat(foundGames.get(i).getWatchers(), equalTo(testGames.get(i).getWatchers()));
        }

        gameRepository.deleteAll();
    }
}
