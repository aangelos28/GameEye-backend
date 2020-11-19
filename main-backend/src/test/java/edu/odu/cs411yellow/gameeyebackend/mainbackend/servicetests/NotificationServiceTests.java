package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class NotificationServiceTests {
    @Autowired
    private GameRepository games;

    @Autowired
    private NotificationService notificationService;

    @Test
    public void testSendArticleNotifications() {
        final List<Article> articles = new ArrayList<>();
        final List<String> articleGameIds = new ArrayList<>();

        // Create test articles
        Article firstArticle = new Article();
        firstArticle.setIsImportant(true);
        firstArticle.setTitle("Fallout 3: New Gameplay Trailer");
        firstArticle.setSnippet("New gameplay trailer released for Fallout 3");
        articles.add(firstArticle);
        articleGameIds.add(games.findGameByTitle("Fallout 3").getId());

        Article secondArticle = new Article();
        secondArticle.setIsImportant(false);
        secondArticle.setTitle("Cyberpunk 2077: Guide to Night City");
        secondArticle.setSnippet("A guide on how to navigate Cyberpunk 2077 Night City");
        articles.add(secondArticle);
        articleGameIds.add(games.findGameByTitle("Cyberpunk 2077").getId());

        Article thirdArticle = new Article();
        thirdArticle.setIsImportant(false);
        thirdArticle.setTitle("Watch Dogs: Legion New Accessories");
        thirdArticle.setSnippet("New phone accessories for Watch Dogs: Legion");
        articles.add(thirdArticle);
        articleGameIds.add(games.findGameByTitle("Watch Dogs: Legion").getId());

        Article fourthArticle = new Article();
        fourthArticle.setIsImportant(true);
        fourthArticle.setTitle("Watch Dogs: Legion Released Today");
        fourthArticle.setSnippet("Watch Dogs: Legion has been released today");
        articles.add(fourthArticle);
        articleGameIds.add(games.findGameByTitle("Watch Dogs: Legion").getId());

        notificationService.sendArticleNotificationsAsync(articles, articleGameIds);
    }
}
