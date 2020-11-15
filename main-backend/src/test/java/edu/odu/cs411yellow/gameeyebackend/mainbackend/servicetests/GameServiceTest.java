package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.ImageResource;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ImageRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbReplicationService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
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
    private IgdbReplicationService replicationService;

    @Autowired
    private IgdbService igdbService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private NewsWebsiteRepository news;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ElasticGameRepository elasticRepository;

    @BeforeEach
    public void insertGames() {
    }

    @Test
    public void testGetLogoUrl() {
        int minId = 100000;
        int maxId = 100152;
        int limit = 250;

        String result = replicationService.replicateGamesByRange(minId, maxId, limit);
        System.out.println(result);

        String gameIgdbId1 = "100000";
        String gameIgdbId2 = "100083";
        String gameIgdbId3 = "100152";

        Game game1 = gameRepository.findByIgdbId(gameIgdbId1);
        Game game2 = gameRepository.findByIgdbId(gameIgdbId2);
        Game game3 = gameRepository.findByIgdbId(gameIgdbId3);

        String logoUrl1 = gameService.getLogoUrl(game1.getId());
        String logoUrl2 = gameService.getLogoUrl(game2.getId());
        String logoUrl3 = gameService.getLogoUrl(game3.getId());

        // Check that logoUrls match
        assertThat(logoUrl1, is(game1.getLogoUrl()));
        assertThat(logoUrl2, is(game2.getLogoUrl()));
        assertThat(logoUrl3, is(game3.getLogoUrl()));

        // Check that logoUrls contain ".jpg"
        assertThat(logoUrl1, containsString(".jpg"));
        assertThat(logoUrl2, containsString(".jpg"));
        assertThat(logoUrl3, containsString(".jpg"));

        // Delete new games from elastic.
        for (int currentId = minId; currentId < maxId + 1; currentId++) {
            if (gameRepository.existsByIgdbId(String.valueOf(currentId))) {
                String gameId = gameRepository.findByIgdbId(String.valueOf(currentId)).getId();
                elasticRepository.deleteByGameId(gameId);
            }
        }

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
        Game newGame = igdbService.getGameById(doomEternalId);
        newGame.setResources(resources);

        gameRepository.insert(newGame);
        Game insertedGame = gameRepository.findGameByTitle(newGame.getTitle());

        assertThat(gameService.findArticles(insertedGame.getId()), is(insertedGame.getResources().getArticles()));

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
