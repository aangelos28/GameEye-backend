package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
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

        System.out.print(gsTest.toString());
        assert (true);
    }


}
