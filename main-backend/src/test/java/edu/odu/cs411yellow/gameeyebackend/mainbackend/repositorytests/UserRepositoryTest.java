package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.Articles;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationCategories;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    User testUser = new User();
    Game testGame = new Game();

    @BeforeEach
    public void insertTestUserIntoGameEyeTest() throws IOException {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        ObjectMapper objectMapper = new ObjectMapper();

        String resourcesPath = "src/test/resources";
        String userJsonFileName = "user.json";
        String userFilePath = resourcesPath + File.separator + userJsonFileName;

        testUser = objectMapper.readValue(new File(userFilePath), User.class);
        userRepository.insert(testUser);

        String gameJsonFileName = "game.json";
        String gameFilePath = resourcesPath + File.separator + gameJsonFileName;

        testGame = objectMapper.readValue(new File(gameFilePath), Game.class);

    }

    @AfterEach
    public void deleteUserFromGameEyeTest() {
        String userId = testUser.getId();
        if (userRepository.existsById(userId))
            userRepository.deleteById(userId);
    }

    @Test
    public void findUserByEmail() {
        String foundUserEmail = testUser.getEmail();
        User foundUser = userRepository.findUserByEmail(foundUserEmail);

        assert(foundUser.getEmail().equals(testUser.getEmail()));

    }

    @Test
    public void findUsersWatchListAndCheckAgainstAvailableGames() {
        String foundUserEmail = testUser.getEmail();
        User foundUser = userRepository.findUserByEmail(foundUserEmail);

        List<WatchedGame> foundUserWatchList = foundUser.getWatchList();
        List<WatchedGame> testUserWatchList = testUser.getWatchList();

        for (int i = 0; i < foundUserWatchList.size(); i++) {
            String foundGameId = foundUserWatchList.get(i).getGameId();
            String testGameId =  testUserWatchList.get(i).getGameId();

            assert(foundGameId.equals(testGameId));
        }

    }

    @Test
    public void findArticlesInUsersWatchList() {
        String foundUserEmail = testUser.getEmail();
        User foundUser = userRepository.findUserByEmail(foundUserEmail);

        List<WatchedGame> foundUserWatchList = foundUser.getWatchList();
        List<WatchedGame> testUserWatchList = testUser.getWatchList();

        for (int i = 0; i < foundUserWatchList.size(); i++) {
            NotificationCategories foundNotificationCategories =
                    foundUserWatchList.get(i).getNotificationCategories();

            NotificationCategories testNotificationCategories =
                    testUserWatchList.get(i).getNotificationCategories();

            Articles foundArticles = foundNotificationCategories.getArticles();
            Articles testArticles = testNotificationCategories.getArticles();

            List<String> foundArticleIds = foundArticles.getArticleIds();
            List<String> testArticleIds = testArticles.getArticleIds();

            for (int j = 0; j < foundArticleIds.size(); j++) {
                String foundArticleId = foundArticleIds.get(j);
                String testArticleId = testArticleIds.get(j);
                assert(foundArticleId.equals(testArticleId));

            }

        }

    }

}