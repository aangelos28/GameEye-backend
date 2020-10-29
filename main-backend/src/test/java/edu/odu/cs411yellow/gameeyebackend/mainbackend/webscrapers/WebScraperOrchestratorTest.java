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
    @Autowired
    IGNScraper ign;
    @Autowired
    PCGamerScraper pc;
    @Autowired
    EuroGamerScraper euro;
    @Autowired
    GameSpotScraper gs;

    @Autowired
    WebScraperOrchestrator scrappy;
    @Autowired
    WebScraperOrchestrator scrappyMock;
    @Autowired
    NewsWebsiteRepository newsWebsiteRepository;

    @BeforeEach
    public void init(){
        scrappyMock = new WebScraperOrchestrator(mock, gs, ign, euro, pc, newsWebsiteRepository);
        //scrappyMock = new WebScraperOrchestrator("GameEye Mock News");
        //WebScraper mock = new MockNewsScraper(newsWebsiteRepository);
        //scrappyMock = new WebScraperOrchestrator(mock);
    }



    @Test
    public void testForceScrape(){
        //TODO
    }

    @Test
    public void testForceScrapeMockNews(){
        scrappyMock.forceScrape("GameEye Mock News");
        System.out.println(scrappyMock.toString());
        assertEquals(mock.toString(),scrappyMock.toString());
        assertEquals(mock.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeGameSpot(){
        scrappyMock.forceScrape("GameSpot");
        System.out.println(scrappyMock.toString());
        assertEquals(gs.toString(),scrappyMock.toString());
        assertEquals(gs.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeIGN(){
        scrappyMock.forceScrape("IGN");
        System.out.println(scrappyMock.toString());
        assertEquals(ign.toString(),scrappyMock.toString());
        assertEquals(ign.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testForceScrapePCGamer(){
        scrappyMock.forceScrape("PC Gamer");
        System.out.println(scrappyMock.toString());
        assertEquals(pc.toString(),scrappyMock.toString());
        assertEquals(pc.getArticles(), scrappyMock.getArticleCollection());
    }

    @Test
    public void testForceScrapeEuroGamer(){
        scrappyMock.forceScrape("Eurogamer");
        System.out.println(scrappyMock.toString());
        assertEquals(euro.toString(),scrappyMock.toString());
        assertEquals(euro.getArticles(), scrappyMock.getArticleCollection());
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
