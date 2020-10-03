package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class EuroGamerScraperTest {

    public EuroGamerScraper egTest;


    @BeforeEach
    public void setUp() {
        egTest = new EuroGamerScraper(new ArrayList<>() {
        });
    }

    //TODO write Unit Tests
    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {

        System.out.print(egTest.toString());
        assert (true);
    }


}
