package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class WebScraperOrchestratorTest {

    @Autowired
    MockNewsScraper mock;

    WebScraperOrchestrator scrappy;
    WebScraperOrchestrator scrappyMock;
    NewsWebsiteRepository newsWebsiteRepository;

    @Before
    public void init(){
        //scrappy = new WebScraperOrchestrator();
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
        //WebScraper mock = new MockNewsScraper(newsWebsiteRepository);
        WebScraper mockOrch = mock;
        //scrappyMock = new WebScraperOrchestrator(mockOrch);
        //scrappyMock = new WebScraperOrchestrator("GameEye Mock News");
        scrappyMock = new WebScraperOrchestrator();
        scrappyMock.forceScrape();
        //System.out.println(scrappyMock.getScrapers());
        //System.out.println(scrappyMock.toString());
        //Assert.noNullElements(scrappyMock.getArticleCollection(), "Error: No Articles Scraped");
        //assertEquals(mock.toString(),scrappyMock.toString());
        //assertEquals(mock.getArticles(), scrappyMock.getArticleCollection());
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
