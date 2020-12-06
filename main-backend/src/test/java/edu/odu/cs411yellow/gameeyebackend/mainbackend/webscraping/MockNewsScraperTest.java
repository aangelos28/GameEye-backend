package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class MockNewsScraperTest {
    @Autowired
    MockNewsScraper mockScraper;

    @Test
    public void testScrape() {
        List<Article> articles = mockScraper.scrape(mockScraper.getScraperName());
        for (Article article: articles) {
            System.out.println(article.toString());
        }

        Assert.noNullElements(articles, "Error: Articles not Scraped");
    }
}
