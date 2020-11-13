package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrchestratorScrapeTest {

    private String[] scraperNames={"GameSpot","Eurogamer","PC Gamer", "IGN","GameEye Mock News"};

    @Autowired
    MockNewsScraper mock;

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
    private ReferenceGameService rgs;
    @Autowired
    private GameRepository games;

    @Autowired
    GameService gameService;

    @BeforeEach
    public void init(){
        orchestratorMock = new WebScraperOrchestrator(scraper, mock, elasticGames, rgs, newsWebsiteRepository,games, gameService);
    }

    @Test
    public void testForceScrape(){
        orchestratorMock.forceScrape();
        System.out.println(orchestratorMock.toString());
        String totalCollection="";

        for(String s: scraperNames){
            scraper.scrape(s);
            totalCollection += scraper.toString()+"\n";
        }

        //assertEquals(totalCollection,orchestratorMock.toString());
    }

    @Test
    public void testForceScrapeMockNews(){
        orchestratorMock.forceScrape(mock);
        //System.out.println(orchestratorMock.toString());

        //assertEquals(mock.toString(),orchestratorMock.toString());
        //assertEquals(mock.getArticles(), orchestratorMock.getArticleCollection());
        assertEquals(mock.getArticles(),orchestratorMock.getAllArticles());
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
}
