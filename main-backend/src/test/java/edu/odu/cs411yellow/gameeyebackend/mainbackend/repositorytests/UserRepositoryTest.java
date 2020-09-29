package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.*;
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

    User testUser;

    @BeforeEach
    public void insertTestUserIntoGameEyeTest() {

        String userId = "5e98dc5da3464d35b824d052";
        String firebaseId = "123456789";
        UserStatus status = UserStatus.inactive;
        UserPlan plan = UserPlan.free;

        // Declare preferences
        boolean showArchivedResources = false;
        boolean showImpactScores = false;

        ContentPreferences contentPreferences = new ContentPreferences(showArchivedResources, showImpactScores);

        List<String> resourceCategories = new ArrayList<>(Arrays.asList("articles"));
        NotificationPreferences notificationPreferences = new NotificationPreferences(resourceCategories);

        Preferences preferences = new Preferences(contentPreferences, notificationPreferences);

        // Declare articles
        int articleCount = 1;
        String articleId = "5ea1c2e777dabd049ce92788";
        List<String> articleIds = new ArrayList<>(Arrays.asList(articleId));
        ArticleNotifications articleNotifications = new ArticleNotifications(articleCount, articleIds);

        // Declare images
        int imageCount = 1;
        String imageId = "5ea108ea34019c1d1c818c02";
        List<String> imageIds = new ArrayList<>(Arrays.asList(imageId));
        ImageNotifications imageNotifications = new ImageNotifications(imageCount, imageIds);

        // Declare notificationCategories
        ResourceNotifications resourceNotifications = new ResourceNotifications(articleNotifications, imageNotifications);

        // Declare watchGame
        String watchedGameId = "5e98bf94a3464d35b824d04f";
        int notificationCount = 1;
        WatchedGame watchedGame = new WatchedGame(watchedGameId, notificationCount, resourceNotifications);

        // Declare watchList
        List<WatchedGame> watchList = new ArrayList<>(Arrays.asList(watchedGame));

        // Set testUser
        testUser = new User(userId, firebaseId, status, plan, preferences, watchList);

        // Write testUser to GameEyeTest
        userRepository.insert(testUser);

    }

    @AfterEach
    public void deleteTestUserFromGameEyeTest() {
        String userId = testUser.getId();
        if (userRepository.existsById(userId))
            userRepository.deleteById(userId);
    }

    @Test
    public void findUserById() {
        String foundUserId = testUser.getId();
        User foundUser = userRepository.findUserById(foundUserId);

        assert(foundUser.getId().equals(testUser.getId()));

    }

    @Test
    public void findUserByFirebaseId() {
        String foundUserFirebaseId = testUser.getFirebaseId();
        User foundUser = userRepository.findUserByFirebaseId(foundUserFirebaseId);

        assert(foundUser.getFirebaseId().equals(testUser.getFirebaseId()));

    }

    @Test
    public void findUsersWatchListAndCheckAgainstAvailableGames() {
        String foundUserId = testUser.getId();
        User foundUser = userRepository.findUserById(foundUserId);

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
        String foundUserId = testUser.getId();

        User foundUser = userRepository.findUserById(foundUserId);

        List<WatchedGame> foundUserWatchList = foundUser.getWatchList();
        List<WatchedGame> testUserWatchList = testUser.getWatchList();

        for (int i = 0; i < foundUserWatchList.size(); i++) {
            ResourceNotifications foundResourceNotifications =
                    foundUserWatchList.get(i).getNotificationCategories();

            ResourceNotifications testResourceNotifications =
                    testUserWatchList.get(i).getNotificationCategories();

            ArticleNotifications foundArticleNotifications = foundResourceNotifications.getArticles();
            ArticleNotifications testArticleNotifications = testResourceNotifications.getArticles();

            List<String> foundArticleIds = foundArticleNotifications.getArticleIds();
            List<String> testArticleIds = testArticleNotifications.getArticleIds();

            for (int j = 0; j < foundArticleIds.size(); j++) {
                String foundArticleId = foundArticleIds.get(j);
                String testArticleId = testArticleIds.get(j);
                assert(foundArticleId.equals(testArticleId));

            }

        }

    }

}