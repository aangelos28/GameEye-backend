package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WebScraperOrchestratorTest {

    private String[] scraperNames = {"GameSpot", "Eurogamer", "PC Gamer", "IGN", "GameEye Mock News"};

    @Autowired
    MockNewsScraper mockNewsScraper;

    List<Article> mockArticles;

    @Autowired
    UniversalScraper scraper;

    @Autowired
    WebScraperOrchestrator orchestrator;
    @Autowired
    WebScraperOrchestrator orchestratorMock;
    @Autowired
    NewsWebsiteRepository newsWebsiteRepository;

    @Autowired
    @Qualifier("elasticsearchOperations")
    ElasticsearchOperations elasticSearch;
    @Autowired
    private ElasticGameRepository elasticGames;


    @Autowired
    private ReferenceGameService referenceGameService;
    @Autowired
    private GameRepository games;

    @Autowired
    GameService gameService;

    private Game mockgame1;
    private Game mockgame2;
    private Game mockgame3;
    private Game mockgame4;


    @Autowired
    private MachineLearningService mlService;

    Article og;

    private String mockScraperName = "GameEye Mock News";

    @BeforeEach
    public void init() {

        Resources mock1Resources = new Resources();
        mockgame1 = new Game();
        mockgame1.setId("WebScraperOrchTest - MockGame1");
        mockgame1.setTitle("Genshin Impact");
        mockgame1.setResources(mock1Resources);

        Resources mock2Resources = new Resources();
        mockgame2 = new Game();
        mockgame2.setId("WebScraperOrchTest - MockGame2");
        mockgame2.setTitle("Cyberpunk 2077");
        mockgame2.setResources(mock2Resources);


        Resources mock3Resources = new Resources();
        mockgame3 = new Game();
        mockgame3.setId("WebScraperOrchTest - MockGame3");
        mockgame3.setTitle("Doom Eternal");
        mockgame3.setResources(mock3Resources);

        mockArticles = mockNewsScraper.scrape(mockScraperName);

        og = new Article(mockArticles.get(8)); //"Destiny 2: Beyond Light adds ice"
        List<Article> arts = new ArrayList<>(Collections.singletonList(og));

        String title = "Destiny 2: Beyond Light";
        mockgame4 = new Game();
        mockgame4.setId("WebScraperOrchTest - MockGame4");
        //mockgame4.setId();
        mockgame4.setTitle(title);
        mockgame4.getResources().setArticles(arts);

        games.save(mockgame1);
        games.save(mockgame2);
        games.save(mockgame3);
        games.save(mockgame4);

        ElasticGame elasticGame1 = new ElasticGame(mockgame1);
        ElasticGame elasticGame2 = new ElasticGame(mockgame2);
        ElasticGame elasticGame3 = new ElasticGame(mockgame3);
        ElasticGame elasticGame4 = new ElasticGame(mockgame4);

        elasticGames.save(elasticGame1);
        elasticGames.save(elasticGame2);
        elasticGames.save(elasticGame3);
        elasticGames.save(elasticGame4);

        orchestratorMock = new WebScraperOrchestrator(scraper, mockNewsScraper, referenceGameService, games, gameService, mlService);
    }

    @AfterEach
    public void deleteTestGames() {
        games.deleteById(mockgame1.getId());
        games.deleteById(mockgame2.getId());
        games.deleteById(mockgame3.getId());
        games.deleteById(mockgame4.getId());

        elasticGames.deleteByGameId(mockgame1.getId());
        elasticGames.deleteByGameId(mockgame2.getId());
        elasticGames.deleteByGameId(mockgame3.getId());
        elasticGames.deleteByGameId(mockgame4.getId());

        mockArticles.clear();
    }

    @Test
    public void testInit() {

    }

    @Test
    public void testCheckArticleDuplicates() {
        String title = mockgame4.getTitle();
        String id = mockgame4.getId();
        System.out.println(title + ": " + id);

        // Check that method returns true for a duplicate article already saved earlier in the test.
        Article dupe = new Article(og);

        assertThat(orchestratorMock.checkArticleDuplicates(id, dupe), is(true));

        // Check that method returns false for a unique articles
        Article unique = new Article(og);
        unique.setTitle("A unique game article title");
        assertThat(orchestratorMock.checkArticleDuplicates(id, unique), is(false));
    }

    @Test
    public void testCheckIrrelevantArticles() {

        Article irrelevant = mockArticles.get(mockArticles.size() - 1);    //"Making sense of the SCAA’s new Flavor Wheel"
        boolean irr = orchestratorMock.checkIrrelevantArticles(irrelevant);
        System.out.println(irrelevant.getTitle());
        System.out.println(irr);

        Article relevant = mockArticles.get(mockArticles.size() - 6);    //"Cyberpunk 2077 Delayed to 12/10/2077
        boolean nonIrr = orchestratorMock.checkIrrelevantArticles(relevant);
        System.out.println(relevant.getTitle());
        System.out.println(nonIrr);


        assertThat(irr, is(true));
        assertThat(nonIrr, is(false));
    }

    @Test
    public void testInsertDataIntoDatabase() {
        // Retrieve game before inserting articles
        Game preTestGame = games.findGameByTitle("Cyberpunk 2077");
        Resources preResources = preTestGame.getResources();
        List<Article> preArticles = preResources.getArticles();

        orchestratorMock.insertArticlesIntoDatabase(mockArticles);

        // Retrieve game after inserting articles
        Game postTestGame = games.findGameByTitle("Cyberpunk 2077");
        Resources postResources = postTestGame.getResources();
        List<Article> postArticles = postResources.getArticles();

        assertNotEquals(preResources, postResources);
        assertNotEquals(preArticles, postArticles);

        assertNotEquals(preArticles.size(), postArticles.size());
    }

   /* @Test
    public void testRemoveFromCollection(){
        orchestratorMock.forceScrape(mock);
        List<Article> beforeRemoval = new ArrayList<>(orchestratorMock.getArticleCollection());
        System.out.println(orchestratorMock.toString());

        orchestratorMock.removeFromCollection(beforeRemoval.get(0));
        List<Article> afterRemoval = new ArrayList<>(orchestratorMock.getArticleCollection());
        System.out.println(orchestratorMock.toString());

        assertThat(afterRemoval, is(not(beforeRemoval)));
    }*/

    @Test
    public void testPerformAGRSForSingleGameMention() {

        List<Article> articles = orchestratorMock.scrape(mockNewsScraper);
        List<String> gameIDs = orchestratorMock.performArticleGameReferenceSearch(articles.get(0));

        System.out.println(articles.get(0).getTitle());
        for (String id : gameIDs) {
            System.out.println("Game ID(s): " + id);
            System.out.println("Game: " + games.findGameByTitle(id));
        }
    }

    @Test
    public void testMockGameRepo() {
        for (Game game : games.findAll()) {
            System.out.println(game.getTitle());
        }
    }

    @Test
    public void testGetGamesArticles() {
        String title = "Destiny 2: Beyond Light";

        List<Article> target = games.findGameByTitle(title).getResources().getArticles();
        for (Article a : target) {
            System.out.println(a.getTitle());
        }
    }


    @Test
    public void testGetArticleImportance() {
        List<String> titles = new ArrayList<>();

        String title1 = "Cyberpunk 2077 releases 12/10/2077";
        String title2 = "Doom Eternal: Where to find all runes";
        String title3 = "Halo Infinite contains microtransactions";
        String title4 = "Destiny 2: Beyond Light releases new Seasonal content";
        String ogTitle = og.getTitle(); //Destiny 2: Beyond Light adds ice

        titles.add(title1);
        titles.add(title2);
        titles.add(title3);
        titles.add(title4);
        titles.add(ogTitle);

        List<Boolean> importScores = orchestratorMock.getArticleImportance(titles);

        assertThat(importScores.get(0), is(true));
        assertThat(importScores.get(1), is(false));
        assertThat(importScores.get(2), is(false));
        assertThat(importScores.get(3), is(true));
        assertThat(importScores.get(4), is(true));
    }

    @Test
    public void testAssignScrapedArticlesImportance() {
        List<String> titles = new ArrayList<>();

        for (Article a : mockArticles) {
            titles.add(a.getTitle());
            System.out.println(a.getTitle());
        }

        orchestratorMock.assignScrapedArticlesImportance(titles, mockArticles);

        assertThat(mockArticles.get(6).getIsImportant(), is(false)); //Cyberpunk 2077: The most hair styles of any RPG
        assertThat(mockArticles.get(7).getIsImportant(), is(true));  //New update will be released in December for Genshin Impact
        assertThat(mockArticles.get(8).getIsImportant(), is(true));  //Destiny 2: Beyond Light adds ice
    }
}
