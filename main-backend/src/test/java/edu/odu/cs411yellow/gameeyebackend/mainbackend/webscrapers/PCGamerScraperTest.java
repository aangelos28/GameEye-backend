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
public class PCGamerScraperTest {

    @Autowired
    PCGamerScraper pcTest;

    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {
        pcTest.scrape();
        System.out.print(pcTest.toString());
        Assert.noNullElements(pcTest.getArticles(), "Error: Articles not Scraped");

    }


}
