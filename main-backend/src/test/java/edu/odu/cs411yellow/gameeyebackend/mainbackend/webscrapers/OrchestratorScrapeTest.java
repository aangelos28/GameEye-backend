package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrchestratorScrapeTest {

    //private String[] scraperNames={"GameSpot","Eurogamer","PC Gamer", "IGN","GameEye Mock News"};
    private String[] scraperNames={"GameSpot","Eurogamer", "PC Gamer","IGN"};

    @Autowired
    MockNewsScraper mockNewsScrapper;

    List<Article> articles;

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

    private String mockScraperName = "GameEye Mock News";


    @Autowired
    private ReferenceGameService rgs;
    @Autowired
    private GameRepository games;

    @Autowired
    GameService gameService;

    @Autowired
    MachineLearningService mlService;

    private Game mockgame1;
    private Game mockgame2;
    private Game mockgame3;
    private Game mockgame4;



    @BeforeEach
    public void init(){
        //orchestratorMock = new WebScraperOrchestrator(scraper, mock, elasticGames, rgs, newsWebsiteRepository, games, gameService);

        mockgame1 = new Game();
        mockgame1.setId("5fa21ab86ece0e1877574053");
        mockgame1.setTitle("Cyberpunk 2077");
        //cyber.setResources();

        mockgame2 = new Game();
        mockgame2.setId("5f8a18f3d2ac2273ccbf4c66");
        mockgame2.setTitle("Fallout 3");

        mockgame3 = new Game();
        mockgame3.setId("5fa622897633227d28b06931");
        mockgame3.setTitle("Watch Dogs: Legion");

        mockgame4 = new Game();
        mockgame4.setId("5fa25fd86ffacd4ab297d3e1");
        mockgame4.setTitle("Destiny 2: Beyond Light");

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

        orchestratorMock = new WebScraperOrchestrator(scraper, mockNewsScrapper, elasticGames, rgs, newsWebsiteRepository, games, gameService, mlService);
    }


    @AfterEach
    public void emptyArticles(){
        articles.clear();
        games.deleteById(mockgame1.getId());
        games.deleteById(mockgame2.getId());
        games.deleteById(mockgame3.getId());
        games.deleteById(mockgame4.getId());

        elasticGames.deleteByGameId(mockgame1.getId());
        elasticGames.deleteByGameId(mockgame2.getId());
        elasticGames.deleteByGameId(mockgame3.getId());
        elasticGames.deleteByGameId(mockgame4.getId());
    }

    @Test
    public void testForceScrape(){
        articles = orchestratorMock.scrapeAll();

        List<Article> totalCollection=new ArrayList<Article>();
        for(String s: scraperNames){
            totalCollection.addAll(scraper.scrape(s));
        }

        totalCollection.addAll(mockNewsScrapper.scrape(mockScraperName));


        //assertEquals(totalCollection.size(),orchestratorMock.getAllArticles().size());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(totalCollection.size())));
    }

    @Test
    public void testForceScrapeMockNews(){

        articles=orchestratorMock.scrape(mockNewsScrapper);
        System.out.println();
        for(Article a:articles)
        {
            System.out.println(a.getTitle());
        }

        int collectionSize = mockNewsScrapper.scrape(mockScraperName).size();

        //assertEquals(mock.getArticles().size(),orchestratorMock.getAllArticles().size());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(collectionSize)));
    }

    @Test
    public void testForceScrapeGameSpot(){
        articles = orchestratorMock.scrape("GameSpot");
        //System.out.println(orchestratorMock.toString());
        int collectionSize = scraper.scrape("GameSpot").size();

        //assertEquals(scraper.toString(),orchestratorMock.toString());
        //assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(collectionSize)));
    }

    @Test
    public void testForceScrapeIGN(){
        articles = orchestratorMock.scrape("IGN");
        System.out.println(orchestratorMock.toString());

        int collectionSize = scraper.scrape("IGN").size();

        //assertEquals(scraper.toString(),orchestratorMock.toString());
        //assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(collectionSize)));
    }

    @Test
    public void testForceScrapePCGamer(){
        articles=orchestratorMock.scrape("PC Gamer");
        //System.out.println(orchestratorMock.toString());
        int collectionSize = scraper.scrape("PC Gamer").size();

        //assertEquals(scraper.toString(),orchestratorMock.toString());
        //assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(collectionSize)));
    }

    @Test
    public void testForceScrapeEuroGamer(){
        articles=orchestratorMock.scrape("Eurogamer");
        //System.out.println(orchestratorMock.toString());
        int collectionSize = scraper.scrape("Eurogamer").size();


        //assertEquals(scraper.toString(),orchestratorMock.toString());
        //assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(collectionSize)));
    }
}
