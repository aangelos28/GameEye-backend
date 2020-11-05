package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepositoryCustomImpl;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.properties")
public class WebScraperOrchestratorTest {

    private String[] scraperNames={"GameSpot","Eurogamer","PC Gamer", "IGN","GameEye Mock News"};

    @Autowired
    MockNewsScraper mock;

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
    private ElasticGameRepositoryCustomImpl elastic;
    @Autowired
    private ReferenceGameService rgs;
    @Autowired
    private GameRepository games;

    private Article testArticle;

    //@Value("http://411Yellow.cpi.cs.odu.edu")
    //@Value("${ml.server.host}")
    private String serverHost;

    //@Value("7745")
    //@Value("${ml.server.port}")
    private Integer serverPort;

    //@Autowired
    //private MachineLearningService machine = new MachineLearningService(
    // @Value("${ml.server.host}") serverHost, @Value("${ml.server.port}") serverPort);

    @BeforeEach
    public void init(){
        //scrappyMock = new WebScraperOrchestrator(scrap, mock, rgs, newsWebsiteRepository);
        orchestratorMock = new WebScraperOrchestrator(scraper, mock, elasticGames, rgs, newsWebsiteRepository,games);
        //scrappyMock = new WebScraperOrchestrator(scrap, mock, elastic, rgs, newsWebsiteRepository);
        //scrappyMock = new WebScraperOrchestrator(scrap, mock, machine, rgs, newsWebsiteRepository);
    }

    @Test
    public void testForceScrape(){
        orchestratorMock.forceScrape();
        String totalCollection="";

        for(String s: scraperNames){
            scraper.scrape(s);
            totalCollection += scraper.toString();
        }

        assertEquals(totalCollection,orchestratorMock.toString());
    }

    @Test
    public void testForceScrapeMockNews(){
        orchestratorMock.forceScrape(mock);
        System.out.println(orchestratorMock.toString());
        assertEquals(mock.toString(),orchestratorMock.toString());
        assertEquals(mock.getArticles(), orchestratorMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeGameSpot(){
        orchestratorMock.forceScrape("GameSpot");
        System.out.println(orchestratorMock.toString());
        assertEquals(scraper.toString(),orchestratorMock.toString());
        assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeIGN(){
        orchestratorMock.forceScrape("IGN");
        System.out.println(orchestratorMock.toString());
        assertEquals(scraper.toString(),orchestratorMock.toString());
        assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
    }

    @Test
    public void testForceScrapePCGamer(){
        orchestratorMock.forceScrape("PC Gamer");
        System.out.println(orchestratorMock.toString());
        assertEquals(scraper.toString(),orchestratorMock.toString());
        assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeEuroGamer(){
        orchestratorMock.forceScrape("Eurogamer");
        System.out.println(orchestratorMock.toString());
        assertEquals(scraper.toString(),orchestratorMock.toString());
        assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
    }

    @Test
    public void testCheckArticleDuplicates(){
        mock.scrape(mock.getScrapperName());
        List<Article> scrapedArticles=mock.getArticles();
        Article dupe = new Article(scrapedArticles.get(1)); //"Cyberpunk 2077 delayed to 12/10/2077"

        orchestratorMock.forceScrape(mock);
        orchestratorMock.insertArticlesIntoDatabase();

        Boolean duped = orchestratorMock.checkArticleDuplicates(dupe);

        assertThat(duped,is(true));

    }

    @Test
    public void testCheckIrrelevantArticles(){
        //orchestratorMock.forceScrape(mock);
        //List<Article> scrapedArticles= orchestratorMock.getArticleCollection();

        mock.scrape(mock.getScrapperName());
        List<Article> scrapedArticles=mock.getArticles();
        Article irrelevant = scrapedArticles.get(3);    //"GameEye Launch"

        //List<String> ids = orchestratorMock.performArticleGameReferenceSearch(irrelevant);
        //String possibleGame = ids.get(0);
        //orchestratorMock.forceScrape(mock);

        boolean irr = orchestratorMock.checkIrrelevantArticles(irrelevant);

        assertThat(irr,is(true));

    }

    @Test
    public void testInsertDataIntoDatabase(){
        Game testGame = games.findGameByTitle("Cyberpunk 2077");
        Resources initResources = testGame.getResources();
        List<Article> initArticles = initResources.getArticles();

        orchestratorMock.forceScrape(mock);
        System.out.println(orchestratorMock.toString());
        //List<Article> testArts = scrappyMock.getArticleCollection();
        orchestratorMock.insertArticlesIntoDatabase();

        Resources postResources = testGame.getResources();
        List<Article> postArticles = initResources.getArticles();

        assertNotEquals(initResources,postResources);
        assertNotEquals(initArticles,postArticles);

        assertEquals(initResources,postResources);
        assertEquals(initArticles,postArticles);

        assertNotEquals(initArticles.size(),postArticles.size());

    }

    @Test
    public void testRemoveFromCollection(){
        orchestratorMock.forceScrape(mock);
        List<Article> beforeRemoval = orchestratorMock.getArticleCollection();
        orchestratorMock.removeFromCollection(beforeRemoval.get(0));
        List<Article> afterRemoval = orchestratorMock.getArticleCollection();

        assertNotEquals(beforeRemoval,afterRemoval);
        assertNotEquals(beforeRemoval.size(),afterRemoval.size());
    }

    @Test
    public void testPerformAGRSForSingleGameMention(){

        orchestratorMock.forceScrape(mock);
        List<Article> articles = orchestratorMock.getArticleCollection();
        List<String> gameIDs = orchestratorMock.performArticleGameReferenceSearch(articles.get(0));
       //String game = gameIDs.get(0);
        System.out.println(articles.get(0).getTitle());
        for(String id:gameIDs){
            System.out.println("Game ID(s): "+id);
            System.out.println("Game: "+games.findGameByTitle(id));
        }


    }

}
