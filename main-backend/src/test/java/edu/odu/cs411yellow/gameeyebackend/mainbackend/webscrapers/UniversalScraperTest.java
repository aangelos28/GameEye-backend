package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UniversalScraperTest {


    @Autowired
    public UniversalScraper test;

    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {
        String site1 = "GameSpot";
        List<Article> articlesGS = test.scrape(site1);
        System.out.print(test.toString(site1));
        Assert.noNullElements(articlesGS, "Error: Articles not Scraped");

        String site2 = "Eurogamer";
        List<Article> articlesEG = test.scrape(site2);
        test.scrape(site2);
        System.out.print(test.toString(site2));
        Assert.noNullElements(articlesEG, "Error: Articles not Scraped");

        String site3 = "IGN";
        List<Article> articlesIGN = test.scrape(site3);
        test.scrape(site3);
        System.out.print(test.toString(site3));
        Assert.noNullElements(articlesIGN, "Error: Articles not Scraped");

        String site4 = "PC Gamer";
        List<Article> articlesPC = test.scrape(site4);
        test.scrape(site4);
        System.out.print(test.toString(site4));
        Assert.noNullElements(articlesPC, "Error: Articles not Scraped");
    }


}
