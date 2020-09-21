package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class NewsWebsiteRepositoryTest {

    @Autowired
    private NewsWebsiteRepository newsWebsiteRepository;

    @Test
    public void saveAndDelete() {
        // Declare parameters for newsWebsite
        ObjectId newsWebsiteId = new ObjectId();
        String newsWebsiteName = "GameSpot";
        byte [] newsSiteLogoRawData = {(byte)10101010};
        Binary newsSiteLogoBinaryData = new Binary(newsSiteLogoRawData);
        String newsSiteUrl = "https://www.gamespot.com/";
        String newsSiteRssUrl = "https://www.gamespot.com/feeds/game-news/";
        Date lastUpdated = new Date(2020, 9, 21);

        // Create a news website
        NewsWebsite newsWebsite = new NewsWebsite(newsWebsiteId.toString() ,newsWebsiteName, newsSiteLogoBinaryData,
                                                  newsSiteUrl, newsSiteRssUrl, lastUpdated);

        // Save news website in database
        newsWebsiteRepository.save(newsWebsite);
        assert(newsWebsiteRepository.existsByName("GameSpot"));

        // Delete news website in database
        newsWebsiteRepository.delete(newsWebsite);
        assert(!newsWebsiteRepository.existsByName("GameSpot"));
    }
}