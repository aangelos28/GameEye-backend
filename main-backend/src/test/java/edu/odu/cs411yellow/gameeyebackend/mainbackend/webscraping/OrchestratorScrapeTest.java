package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.NotificationService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
public class OrchestratorScrapeTest {
    String productionConnectionString;

    @Autowired
    public OrchestratorScrapeTest(@Value("${mongodb.prod.connection-string}") String productionConnectionString) {
        this.productionConnectionString = productionConnectionString;
    }

    final private String[] scraperNames = {"GameSpot", "Eurogamer", "PC Gamer", "IGN"};

    @Autowired
    MockNewsScraper mockNewsScraper;

    List<Article> articles;

    @Autowired
    UniversalScraper scraper;

    @Autowired
    WebScraperOrchestrator orchestrator;

    @Autowired
    WebScraperOrchestrator orchestratorMock;

    @Autowired
    NewsWebsiteRepository newsWebsiteRepository;

    private final String mockScraperName = "GameEye Mock News";

    @Autowired
    GameRepository testGamesRepository;

    @Autowired
    GameService gameService;

    @Autowired
    MachineLearningService mlService;

    @Autowired
    NotificationService notificationService;

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

        assertThat(testGamesRepository.count(), is(not(0)));
    }

    @AfterAll
    public void deleteGames() {
        testGamesRepository.deleteAll();
    }

    @BeforeEach
    public void init() {
        testGamesRepository.deleteArticlesFromAllGames();
    }

    @AfterEach
    public void emptyArticles() {
        testGamesRepository.deleteArticlesFromAllGames();
        articles.clear();
    }

    @Test
    public void testForceScrape() {
        articles = orchestratorMock.scrapeAll();

        List<Article> totalCollection = new ArrayList<>();
        for (String s : scraperNames) {
            totalCollection.addAll(scraper.scrape(s));
        }

        totalCollection.addAll(mockNewsScraper.scrape(mockScraperName));

        assertThat(articles.size(), is(greaterThan(0)));
        assertThat(articles.size(), is(lessThanOrEqualTo(totalCollection.size())));

        for (Article article: articles) {
            System.out.println(article.toString());
        }
    }

    @Test
    public void testForceScrapeMockNews() {
        articles = orchestratorMock.scrape(mockNewsScraper);
        System.out.println();
        for (Article a : articles) {
            System.out.println(a.getTitle());
        }

        int collectionSize = mockNewsScraper.scrape(mockScraperName).size();

        assertThat(articles.size(), is(greaterThan(0)));
        assertThat(articles.size(), is(lessThanOrEqualTo(collectionSize)));

        for (Article article: articles) {
            System.out.println(article.toString());
        }
    }

    @Test
    public void testForceScrapeGameSpot() {
        articles = orchestratorMock.scrape("GameSpot");
        int collectionSize = scraper.scrape("GameSpot").size();

        assertThat(articles.size(), is(greaterThan(0)));
        assertThat(articles.size(), is(lessThanOrEqualTo(collectionSize)));

        for (Article article: articles) {
            System.out.println(article.toString());
        }
    }

    @Test
    public void testForceScrapeIGN() {
        articles = orchestratorMock.scrape("IGN");
        System.out.println(orchestratorMock.toString());

        int collectionSize = scraper.scrape("IGN").size();

        assertThat(articles.size(), is(greaterThan(0)));
        assertThat(articles.size(), is(lessThanOrEqualTo(collectionSize)));

        for (Article article: articles) {
            System.out.println(article.toString());
        }
    }

    @Test
    public void testForceScrapePCGamer() {
        articles = orchestratorMock.scrape("PC Gamer");
        int collectionSize = scraper.scrape("PC Gamer").size();

        assertThat(articles.size(), is(greaterThan(0)));
        assertThat(articles.size(), is(lessThanOrEqualTo(collectionSize)));

        for (Article article: articles) {
            System.out.println(article.toString());
        }
    }

    @Test
    public void testForceScrapeEuroGamer() {
        articles = orchestratorMock.scrape("Eurogamer");
        int collectionSize = scraper.scrape("Eurogamer").size();

        assertThat(articles.size(), is(greaterThan(0)));
        assertThat(articles.size(), is(lessThanOrEqualTo(collectionSize)));

        for (Article article: articles) {
            System.out.println(article.toString());
        }
    }
}