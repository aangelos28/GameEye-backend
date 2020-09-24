package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.Articles;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ContentPreferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationCategories;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationPreferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
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

    User testUser;

    @BeforeEach
    public void insertTestUserIntoGameEyeTest() {

        String userId = "5e98dc5da3464d35b824d052";
        String firstName = "Jacob";
        String lastName = "Cook";
        String email = "jcook006@odu.edu";
        UserStatus status = UserStatus.inactive;
        UserPlan plan = UserPlan.free;

        // Declare preferences
        boolean showArchivedResources = false;
        ContentPreferences contentPreferences = new ContentPreferences(showArchivedResources);

        List<String> resourceCategories = new ArrayList<>(Arrays.asList("articles"));
        NotificationPreferences notificationPreferences = new NotificationPreferences(resourceCategories);

        Preferences preferences = new Preferences(contentPreferences, notificationPreferences);

        // Declare articles
        int articleCount = 1;
        String articleId = "5ea1c2e777dabd049ce92788";
        List<String> articleIds = new ArrayList<>(Arrays.asList(articleId));
        Articles articles = new Articles(articleCount, articleIds);

        NotificationCategories notificationCategories = new NotificationCategories(articles);

        // Declare watchGame
        String watchedGameId = "5e98bf94a3464d35b824d04f";
        int notificationCount = 1;
        WatchedGame watchedGame = new WatchedGame(watchedGameId, notificationCount, notificationCategories);

        // Declare watchList
        List<WatchedGame> watchList = new ArrayList<>(Arrays.asList(watchedGame));

        // Set testUser
        testUser = new User(userId, firstName, lastName, email, status, plan, preferences, watchList);

        // Write testUser to GameEyeTest
        userRepository.insert(testUser);

    }

    @AfterEach
    public void deleteTestUserFromGameEyeTest() {
        String userEmail = testUser.getEmail();
        if (userRepository.existsByEmail(userEmail))
            userRepository.deleteByEmail(userEmail);
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
        System.out.println(foundUserEmail + "this is the email");
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