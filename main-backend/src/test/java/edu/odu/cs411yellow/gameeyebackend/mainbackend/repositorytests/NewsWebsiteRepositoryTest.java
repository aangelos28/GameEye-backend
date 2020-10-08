package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private NewsWebsiteRepository newsWebsites;

    NewsWebsite insertedNewsWebsite;

    @BeforeEach
    public void insertNewsWebsiteIntoGameEyeTest() {
        // Declare newsWebsite
        String newsWebsiteId = "5e9fbb092937d83b902ec992";
        String newsWebsiteName = "IGN";
        Binary newsWebsiteLogo = new Binary(new byte[1]);
        String newsWebsiteSiteUrl = "https://www.ign.com/";
        String newsWebsiteRssFeedUrl = "https://corp.ign.com/feeds";
        Date newsWebsiteLastUpdated = new Date(120, 4, 21);

        // Create a news website
        insertedNewsWebsite = new NewsWebsite(newsWebsiteId, newsWebsiteName,
                                              newsWebsiteLogo, newsWebsiteSiteUrl,
                                              newsWebsiteRssFeedUrl, newsWebsiteLastUpdated);

        // Save news website in database
        newsWebsites.save(insertedNewsWebsite);
    }

    @AfterEach
    public void deleteInsertedNewsWebsiteFromGameEyeTest() {
        String newsWebsiteId = insertedNewsWebsite.getId();

        if (newsWebsites.existsNewsWebsiteById(newsWebsiteId))
            newsWebsites.delete(insertedNewsWebsite);

    }

    @Test
    public void findNewsWebsiteById() {
        String newsWebsiteId = insertedNewsWebsite.getId();

        NewsWebsite foundNewsWebsite = newsWebsites.findNewsWebsiteById(newsWebsiteId);

        assert(foundNewsWebsite.getId().equals(insertedNewsWebsite.getId()));
        assert(foundNewsWebsite.getName().equals(insertedNewsWebsite.getName()));
        assert(foundNewsWebsite.getLogo().equals(insertedNewsWebsite.getLogo()));
        assert(foundNewsWebsite.getSiteUrl().equals(insertedNewsWebsite.getSiteUrl()));
        assert(foundNewsWebsite.getRssFeedUrl().equals(insertedNewsWebsite.getRssFeedUrl()));
        assert(foundNewsWebsite.getLastUpdated().equals(insertedNewsWebsite.getLastUpdated()));

    }
}
