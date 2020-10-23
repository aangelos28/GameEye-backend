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
public class UniversalScraperTest {


    @Autowired
    public UniversalScraper test;

    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {
        test.scrape("GameSpot");
        System.out.print(test.toString());
        Assert.noNullElements(test.getArticles(), "Error: Articles not Scraped");

        test.scrape("Eurogamer");
        System.out.print(test.toString());
        Assert.noNullElements(test.getArticles(), "Error: Articles not Scraped");

        test.scrape("IGN");
        System.out.print(test.toString());
        Assert.noNullElements(test.getArticles(), "Error: Articles not Scraped");

        test.scrape("PC Gamer");
        System.out.print(test.toString());
        Assert.noNullElements(test.getArticles(), "Error: Articles not Scraped");
    }


}
