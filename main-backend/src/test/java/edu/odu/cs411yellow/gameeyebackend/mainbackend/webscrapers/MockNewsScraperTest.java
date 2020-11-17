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
public class MockNewsScraperTest {


    @Autowired
    MockNewsScraper MNtest;

    @Test
    public void testScrape() {

        List<Article> articles = MNtest.scrape(MNtest.getScraperName());
        //System.out.print(MNtest.toString());
        for(Article a:articles)
        {
            System.out.println(a);
        }

        Assert.noNullElements(articles, "Error: Articles not Scraped");

    }


}
