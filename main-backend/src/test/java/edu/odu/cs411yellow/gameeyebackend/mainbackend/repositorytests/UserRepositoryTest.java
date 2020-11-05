package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings.*;
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
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    User testUser;

    @BeforeEach
    public void insertTestUserIntoGameEyeTest() throws Exception {

        String userId = "5e98dc5da3464d35b824d052";
        UserStatus status = UserStatus.inactive;
        UserPlan plan = UserPlan.free;

        boolean showArticleResources = true;
        boolean showImageResources = true;
        boolean notifyOnlyIfImportant = true;

        NotificationSettings notificationSettings = new NotificationSettings(showArticleResources,
                                                                                      showImageResources,
                                                                                      notifyOnlyIfImportant);

        Settings settings = new Settings(notificationSettings);

        // Declare articles
        int articleCount = 1;
        String articleId = "5ea1c2e777dabd049ce92788";
        List<String> articleIds = new ArrayList<>();
        articleIds.add(articleId);

        ArticleNotifications articleNotifications = new ArticleNotifications(articleIds);

        // Declare images
        int imageCount = 1;
        String imageId = "5ea108ea34019c1d1c818c02";
        List<String> imageIds = new ArrayList<>();
        imageIds.add(imageId);

        ImageNotifications imageNotifications = new ImageNotifications(imageIds);

        // Declare notificationCategories
        ResourceNotifications resourceNotifications = new ResourceNotifications(articleNotifications, imageNotifications);

        // Declare watchGame
        String watchedGameId = "5e98bf94a3464d35b824d04f";
        int notificationCount = 1;
        WatchedGame watchedGame = new WatchedGame(watchedGameId, resourceNotifications);

        // Declare watchList
        List<WatchedGame> watchList = new ArrayList<>();
        watchList.add(watchedGame);

        // Set testUser
        testUser = new User(userId, status, plan, settings, watchList);

        // Write testUser to GameEyeTest
        users.insert(testUser);
    }

    @AfterEach
    public void deleteTestUserFromGameEyeTest() {
        String userId = testUser.getId();
        if (users.existsById(userId))
            users.deleteById(userId);
    }

    @Test
    public void findUserById() {
        String foundUserId = testUser.getId();
        User foundUser = users.findUserById(foundUserId);

        assert(foundUser.getId().equals(testUser.getId()));
    }

    @Test
    public void testFindUserById() {
        String id = testUser.getId();
        User foundUser = users.findUserById(id);

        assert(foundUser.getId().equals(testUser.getId()));
    }

    @Test
    public void findUsersWatchListAndCheckAgainstAvailableGames() {
        String foundUserId = testUser.getId();
        User foundUser = users.findUserById(foundUserId);

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

        User foundUser = users.findUserById(foundUserId);

        List<WatchedGame> foundUserWatchList = foundUser.getWatchList();
        List<WatchedGame> testUserWatchList = testUser.getWatchList();

        for (int i = 0; i < foundUserWatchList.size(); i++) {
            ResourceNotifications foundResourceNotifications =
                    foundUserWatchList.get(i).getResourceNotifications();

            ResourceNotifications testResourceNotifications =
                    testUserWatchList.get(i).getResourceNotifications();

            ArticleNotifications foundArticleNotifications = foundResourceNotifications.getArticleNotifications();
            ArticleNotifications testArticleNotifications = testResourceNotifications.getArticleNotifications();

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
