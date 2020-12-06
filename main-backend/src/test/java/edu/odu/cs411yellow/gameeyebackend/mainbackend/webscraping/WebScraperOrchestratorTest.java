package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebScraperOrchestratorTest {
    String productionConnectionString;

    @Autowired
    public WebScraperOrchestratorTest(@Value("${mongodb.prod.connection-string}") String productionConnectionString) {
        this.productionConnectionString = productionConnectionString;
    }

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
    private GameRepository testGameRepository;

    @Autowired
    GameService gameService;

    private Game mockgame1;
    private Game mockgame2;
    private Game mockgame3;
    private Game mockgame4;

    Article originalArticle;
    final String articleTitle = "Destiny 2: Beyond Light adds ice";

    final private String mockScraperName = "GameEye Mock News";

    @BeforeAll
    public void setupTestDatabase() throws IOException, InterruptedException {
        System.out.println("Setting up the test database and deleting all articles from all games.");

        StringBuilder builder = new StringBuilder();
        String pathSeparator = File.separator;
        String currentDirectory = Paths.get("").toAbsolutePath().toString();

        builder.append(currentDirectory);
        builder.append(pathSeparator);
        builder.append("src");
        builder.append(pathSeparator);
        builder.append("test");
        builder.append(pathSeparator);
        builder.append("resources");

        String pathToResources = builder.toString();

        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(pathToResources));

        pb.command("sh", "cloneGamesFromProdToTest.sh", productionConnectionString);
        Process p = pb.start();
        p.waitFor();

        assertThat(testGameRepository.count(), is(not(0)));

        testGameRepository.deleteArticlesFromAllGames();
        mockArticles = mockNewsScraper.scrape(mockScraperName);

        // Find and save the article matching articleTitle
        for (Article article: mockArticles) {
            if (article.getTitle().equals(articleTitle)) {
                originalArticle = article;
            }
        }

        mockgame1 = testGameRepository.findByTitle("Genshin Impact");
        mockgame2 = testGameRepository.findByTitle("Cyberpunk 2077");
        mockgame3 = testGameRepository.findByTitle("Doom Eternal");
        mockgame4 = testGameRepository.findByTitle("Destiny 2: Beyond Light");
    }

    @AfterEach
    public void deleteArticles() {
        testGameRepository.deleteArticlesFromAllGames();
    }

    @AfterAll
    public void deleteTestGames() {
        testGameRepository.deleteAll();
    }

    @Test
    public void testCheckArticleDuplicates() {
        String title = mockgame4.getTitle();
        String id = mockgame4.getId();
        System.out.println(title + ": " + id);

        // Add article to Destiny 2: Beyond Light
        gameService.addArticleToGame(originalArticle, mockgame4.getId());

        // Check that method returns true for a duplicate article already saved earlier in the test.
        Article dupe = new Article(originalArticle);

        assertThat(orchestratorMock.checkArticleDuplicates(id, dupe), is(true));

        // Check that method returns false for a unique articles
        Article unique = new Article(originalArticle);
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
        Game preTestGame = testGameRepository.findGameByTitle("Cyberpunk 2077");
        List<Article> preArticles = preTestGame.getResources().getArticles();

        orchestratorMock.insertArticlesIntoDatabase(mockArticles);

        // Retrieve game after inserting articles
        Game postTestGame = testGameRepository.findGameByTitle("Cyberpunk 2077");
        List<Article> postArticles = postTestGame.getResources().getArticles();

        assertNotEquals(preArticles, postArticles);
        assertNotEquals(preArticles, postArticles);
        assertNotEquals(preArticles.size(), postArticles.size());
    }

    @Test
    public void testPerformAGRSForSingleGameMention() {
        List<Article> articles = orchestratorMock.scrape(mockNewsScraper);
        List<String> gameIDs = orchestratorMock.performArticleGameReferenceSearch(articles.get(0));

        System.out.println(articles.get(0).getTitle());
        for (String id : gameIDs) {
            System.out.println("Game ID(s): " + id);
            System.out.println("Game: " + testGameRepository.findGameById(id).toString());
        }
    }

    @Test
    public void testGetGamesArticles() {
        String title = "Destiny 2: Beyond Light";

        List<Article> target = testGameRepository.findGameByTitle(title).getResources().getArticles();
        for (Article article : target) {
            System.out.println(article.toString());
        }
    }

    @Test
    public void testGetArticleImportance() {
        List<String> titles = new ArrayList<>();

        String title1 = "Cyberpunk 2077 releases 12/10/2077";
        String title2 = "Doom Eternal: Where to find all runes";
        String title3 = "Halo Infinite contains microtransactions";
        String title4 = "Destiny 2: Beyond Light releases new Seasonal content";
        String originalArticleTitle = originalArticle.getTitle(); //Destiny 2: Beyond Light adds ice

        titles.add(title1);
        titles.add(title2);
        titles.add(title3);
        titles.add(title4);
        titles.add(originalArticleTitle);

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

        String unimportantArticleTitle = "Cyberpunk 2077: The most hair styles of any RPG";
        Article unimportantArticle = new Article();

        String importantArticleTitle1 = "New update will be released in December for Genshin Impact";
        Article importantArticle1 = new Article();

        String importantArticleTitle2 = articleTitle;
        Article importantArticle2 = new Article();

        // Add titles from mockArticles to titles list
        for (Article article : mockArticles) {
            titles.add(article.getTitle());
        }

        // Assign importance scores
        orchestratorMock.assignScrapedArticlesImportance(titles, mockArticles);

        // Find and assign articles from mockArticles to article objects
        for (Article article : mockArticles) {
            if (article.getTitle().equals(unimportantArticleTitle)) {
                unimportantArticle = article;
            } else if (article.getTitle().equals(importantArticleTitle1)) {
                importantArticle1 = article;
            } else if (article.getTitle().equals(importantArticleTitle2)) {
                importantArticle2 = article;
            }
        }

        assertThat(unimportantArticle.getIsImportant(), is(false));
        assertThat(importantArticle1.getIsImportant(), is(true));
        assertThat(importantArticle2.getIsImportant(), is(true));
    }
}
