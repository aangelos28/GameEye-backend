package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.TestController;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.logging.Logger;

@SpringBootTest
public class GameSpotWebScraperTests {

    GameSpotWebScraper gsTest;
    Logger logger  = (Logger) LoggerFactory.getLogger(TestController.class);
    //TODO Access the test controller

    @BeforeEach
    public void setUp() {
        gsTest = new GameSpotWebScraper(new ArrayList<>());
    }

    //TODO write Unit Tests
    @Test
    public void testScrape() {

        Element firstArticle;

        gsTest.scrape();
        System.out.print(gsTest.toString());
        Assert.noNullElements(gsTest.getArticles(), "Error: Articles not Saved");

//        //Take snapShot of RSS feed
//        try {
//            gsTest.scrape();
//            System.out.print(gsTest.toString());
//            Assert.noNullElements(gsTest.getArticles(), "Error: Articles not Saved");
//
//        }
//        catch (Exception ex){
//            ex.printStackTrace();
//        }

    }


}
