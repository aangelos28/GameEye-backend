package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
//import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WebScraperOrchestratorTest {

    @Autowired
    MockNewsScraper mock;
    //@Autowired
    //IGNScraper ign;
    //@Autowired
    //PCGamerScraper pc;
    //@Autowired
    //EuroGamerScraper euro;
    //@Autowired
    //GameSpotScraper gs;

    @Autowired
    UniversalScraper scrap;

    @Autowired
    WebScraperOrchestrator scrappy;
    @Autowired
    WebScraperOrchestrator scrappyMock;
    @Autowired
    NewsWebsiteRepository newsWebsiteRepository;

    @BeforeEach
    public void init(){
        //scrappyMock = new WebScraperOrchestrator(mock, gs, ign, euro, pc, newsWebsiteRepository);
        scrappyMock = new WebScraperOrchestrator(scrap, mock, newsWebsiteRepository);
    }

    @Test
    public void testForceScrape(){
        //TODO
    }

    @Test
    public void testForceScrapeMockNews(){
        scrappyMock.forceScrape(mock);
        System.out.println(scrappyMock.toString());
        assertEquals(mock.toString(),scrappyMock.toString());
        assertEquals(mock.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeGameSpot(){
        scrappyMock.forceScrape("GameSpot");
        System.out.println(scrappyMock.toString());
        assertEquals(scrap.toString(),scrappyMock.toString());
        assertEquals(scrap.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeIGN(){
        //TODO
        // Consult Jacob regarding IGN listing in database

        scrappyMock.forceScrape("IGN");
        System.out.println(scrappyMock.toString());
        assertEquals(scrap.toString(),scrappyMock.toString());
        assertEquals(scrap.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testForceScrapePCGamer(){
        scrappyMock.forceScrape("PC Gamer");
        System.out.println(scrappyMock.toString());
        assertEquals(scrap.toString(),scrappyMock.toString());
        assertEquals(scrap.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeEuroGamer(){
        scrappyMock.forceScrape("Eurogamer");
        System.out.println(scrappyMock.toString());
        assertEquals(scrap.toString(),scrappyMock.toString());
        assertEquals(scrap.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testCheckArticleDuplicates(){
        //TODO
    }

    @Test
    public void testCheckIrrelevantArticles(){
        //TODO
    }

    @Test
    public void testInsertDataIntoDatabase(){
        //TODO
    }

    @Test
    public void testRemoveFromCollection(){
        //TODO
    }

}
