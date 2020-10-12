package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GameSpotScraperTest {

    @Autowired
    GameSpotScraper gsTest;

    @Test
    public void testScrape() {

        gsTest.scrape();
        System.out.print(gsTest.toString());
        Assert.noNullElements(gsTest.getArticles(), "Error: Articles not Scraped");


    }


}
