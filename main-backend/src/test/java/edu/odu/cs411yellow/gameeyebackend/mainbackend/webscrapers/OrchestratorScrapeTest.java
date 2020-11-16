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


    @Autowired
    private ReferenceGameService rgs;
    @Autowired
    private GameRepository games;

    @Autowired
    GameService gameService;

    @Autowired
    MachineLearningService mlService;



    @BeforeEach
    public void init(){
        //orchestratorMock = new WebScraperOrchestrator(scraper, mock, elasticGames, rgs, newsWebsiteRepository, games, gameService);
        orchestratorMock = new WebScraperOrchestrator(scraper, mockNewsScrapper, elasticGames, rgs, newsWebsiteRepository, games, gameService, mlService);
    }


    @AfterEach
    public void emptyArticles(){
        articles.clear();
    }

    @Test
    public void testForceScrape(){
        articles = orchestratorMock.scrapeAll();

        List<Article> totalCollection=new ArrayList<Article>();
        for(String s: scraperNames){
            scraper.scrape(s);
            totalCollection.addAll(scraper.getArticles());
            scraper.emptyArticles();
        }

        mockNewsScrapper.emptyArticles();
        mockNewsScrapper.scrape(mockNewsScrapper.getScraperName());
        totalCollection.addAll(mockNewsScrapper.getArticles());


        //assertEquals(totalCollection.size(),orchestratorMock.getAllArticles().size());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(totalCollection.size())));
    }

    @Test
    public void testForceScrapeMockNews(){

        articles=orchestratorMock.scrape(mockNewsScrapper);
        mockNewsScrapper.scrape(mockNewsScrapper.getScraperName());

        //assertEquals(mock.getArticles().size(),orchestratorMock.getAllArticles().size());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(mockNewsScrapper.getArticles().size())));
    }

    @Test
    public void testForceScrapeGameSpot(){
        articles = orchestratorMock.scrape("GameSpot");
        //System.out.println(orchestratorMock.toString());

        scraper.scrape("GameSpot");

        //assertEquals(scraper.toString(),orchestratorMock.toString());
        //assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(scraper.getArticles().size())));
    }

    @Test
    public void testForceScrapeIGN(){
        orchestratorMock.scrape("IGN");
        System.out.println(orchestratorMock.toString());

        scraper.scrape("IGN");

        //assertEquals(scraper.toString(),orchestratorMock.toString());
        //assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(scraper.getArticles().size())));
    }

    @Test
    public void testForceScrapePCGamer(){
        articles=orchestratorMock.scrape("PC Gamer");
        //System.out.println(orchestratorMock.toString());

        scraper.scrape("PC Gamer");

        //assertEquals(scraper.toString(),orchestratorMock.toString());
        //assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(scraper.getArticles().size())));
    }

    @Test
    public void testForceScrapeEuroGamer(){
        articles=orchestratorMock.scrape("Eurogamer");
        //System.out.println(orchestratorMock.toString());

        scraper.scrape("EuroGamer");

        //assertEquals(scraper.toString(),orchestratorMock.toString());
        //assertEquals(scraper.getArticles(), orchestratorMock.getArticleCollection());
        assertThat(articles.size(),is(greaterThan(0)));
        assertThat(articles.size(),is(lessThanOrEqualTo(scraper.getArticles().size())));
    }
}
