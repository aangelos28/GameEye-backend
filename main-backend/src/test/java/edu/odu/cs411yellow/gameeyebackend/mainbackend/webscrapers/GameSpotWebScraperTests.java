package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameSpotWebScraperTests {

    private GameSpotWebScraper testScrape;

    @BeforeAll
    public void setUp() {
        testScrape = new GameSpotWebScraper();

    }

    @Test
    public void testScrape() {


        assert (true);
    }

}
