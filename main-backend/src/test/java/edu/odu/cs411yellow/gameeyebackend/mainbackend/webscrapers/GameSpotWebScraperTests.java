package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameSpotWebScraperTests {

    GameSpotWebScraper gsTest;

    //TODO Access the test controller

    @BeforeEach
    public void setUp() {
        gsTest = new GameSpotWebScraper();
    }

    //TODO write Unit Tests
    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {

        Element firstArticle;

        //Take snapShot of RSS feed
        try {
            Document feed = Jsoup.connect("https://www.gamespot.com/feeds/game-news/").get();
            //Testing the Contents of the first article
//            firstArticle = feed.selectFirst("items");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        //Compare to first Article
//        Article one = gsTest.getArticles().get(0);
//        String title = firstArticle.select("title").text();
//        Assert.

        System.out.print(gsTest.toString());
        assert (true);
    }


}
