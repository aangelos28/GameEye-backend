package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
public class PCGamerScraperTest {

    PCGamerScraper pcTest;


    @BeforeEach
    public void setUp() {
        pcTest = new PCGamerScraper(new ArrayList<>());
    }

    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {
        pcTest.scrape();
        System.out.print(pcTest.toString());
        Assert.noNullElements(pcTest.getArticles(), "Error: Articles not Scraped");
        assert (true);
    }


}
