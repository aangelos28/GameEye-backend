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
public class IGNScraperTest {

    @Autowired
    IGNScraper ignTest;

    @Test
    public void testScrape(){
        ignTest.scrape();
        System.out.println(ignTest.toString());
        Assert.noNullElements(ignTest.getArticles(), "Error: Articles not Scraped");
    }
}