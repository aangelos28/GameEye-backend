package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class NewsWebsiteRepositoryTest {

    @Autowired
    private NewsWebsiteRepository newsWebsiteRepository;

    @Test
    public void saveAndDelete() {
        // Create a news website
        NewsWebsite newsWebsite = new NewsWebsite("1234", "GameSpot", null, null, null, null);

        // Save news website in database
        newsWebsiteRepository.save(newsWebsite);
        assert(newsWebsiteRepository.existsById("1234"));

        // Delete news website in database
        newsWebsiteRepository.delete(newsWebsite);
        assert(!newsWebsiteRepository.existsById("1234"));
    }
}