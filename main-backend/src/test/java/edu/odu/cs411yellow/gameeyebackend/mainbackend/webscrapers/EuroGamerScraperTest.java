package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
public class EuroGamerScraperTest {

    public EuroGamerScraper egTest;


    @BeforeEach
    public void setUp() {
        egTest = new EuroGamerScraper(new ArrayList<>() {
        });
    }

    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {
        egTest.scrape();
        System.out.print(egTest.toString());
        Assert.noNullElements(egTest.getArticles(), "Error: Articles not Scraped");
        assert (true);
    }


}
