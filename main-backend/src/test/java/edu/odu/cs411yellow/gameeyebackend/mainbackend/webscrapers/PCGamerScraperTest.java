package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;


@SpringBootTest
public class PCGamerScraperTest {

    PCGamerScraper pcTest;

    @BeforeEach
    public void setUp() {
        pcTest = new PCGamerScraper(new ArrayList<>());
    }


    //Take a SnapShot of the Rss feed
    @Test
    public void testScrape() {

        System.out.print(pcTest.toString());
        assert (true);
    }


}
