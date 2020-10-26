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
public class WebScraperOrchestratorTest {

    @Autowired
    WebScraperOrchestrator scrappy;

    @Test
    public void testForceScrape(){
        //TODO
    }

    @Test
    public void testCheckArticleDuplicates(){
        //TODO
    }

    @Test
    public void testCheckIrrelevantArticles(){
        //TODO
    }

    @Test
    public void testInsertDataIntoDatabase(){
        //TODO
    }

    @Test
    public void testRemoveFromCollection(){
        //TODO
    }

}
