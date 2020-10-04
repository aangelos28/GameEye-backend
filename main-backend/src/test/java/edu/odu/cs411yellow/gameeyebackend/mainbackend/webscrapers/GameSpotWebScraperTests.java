package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GameSpotWebScraperTests {

    GameSpotWebScraper gsTest;
//    Logger logger  = (Logger) LoggerFactory.getLogger(TestController.class);

    @BeforeEach
    public void setUp() {
        gsTest = new GameSpotWebScraper(new ArrayList<>());
    }

    //TODO write Unit Tests
    @Test
    public void testScrape() {

        gsTest.scrape();
        System.out.print(gsTest.toString());
        Assert.noNullElements(gsTest.getArticles(), "Error: Articles not Scraped");


    }


}
