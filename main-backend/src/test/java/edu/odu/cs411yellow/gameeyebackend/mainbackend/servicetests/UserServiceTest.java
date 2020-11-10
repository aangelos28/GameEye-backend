package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Settings;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.UserStatus;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.notifications.ArticleNotifications;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings.NotificationSettings;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.UserService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.WatchlistService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {
    @Autowired
    UserRepository users;

    @Autowired
    UserService userService;

    @Autowired
    WatchlistService watchlistService;

    @Autowired
    IgdbService igdbService;

    @Autowired
    GameService gameService;

    @Autowired
    GameRepository gameRepository;

    @Test
    public void createUserTest() {
        final String userId = "UserServiceTest - createUserTest";

        // Create the user
        userService.createUser(userId);

        // Get the user
        User user = userService.getUser(userId);

        assertThat(user.getId(), is(userId));

        // Delete the user
        userService.deleteUser(userId);

        // Ensure that the user is deleted
        assertThat(userService.checkUserExists(userId), is(false));
    }

    @Test
    public void userStatusTest() {
        final String userId = "UserServiceTest - userStatusTest";

        // Create the user
        userService.createUser(userId);

        // Get the user
        User user = userService.getUser(userId);

        assertThat(user.getStatus(), is(UserStatus.active));

        // Disable user
        userService.setUserStatus(userId, UserStatus.inactive);
        user = userService.getUser(userId);

        assertThat(user.getStatus(), is(UserStatus.inactive));

        // Delete the user
        userService.deleteUser(userId);
    }

    @Test
    public void removeArticleNotifications() throws Exception {
        final String userId = "UserServiceTest - removeArticlesTest";

        // Create the user
        userService.createUser(userId);

        // Create game in mongo from IGDB
        int igdbId1 = 1;
        Game game1 = new Game(igdbService.getGameResponseById(igdbId1));
        game1.setId(ObjectId.get().toString());
        gameService.save(game1);

        // Add game to user's watchlist
        watchlistService.addWatchlistGame(userId, game1.getId());

        // Create article ids
        String articleId1 = "UserServiceTest - articleId1";
        String articleId2 = "UserServiceTest - articleId2";
        String articleId3 = "UserServiceTest - articleId3";

        List<String> articleIds = new ArrayList<>(Arrays.asList(articleId1, articleId2, articleId3));

        // Add articles to user notifications for given game
        userService.addUserArticleNotifications(userId, game1.getId(), articleIds);

        // Remove articles from user notifications for given game
        userService.removeUserArticleNotifications(userId, game1.getId(),articleIds);

        // Check that there are no articles in notifications for given game
        User user = userService.getUser(userId);
        List<WatchedGame> watchedList = user.getWatchList();
        for (WatchedGame watchedGame: watchedList) {
            if (watchedGame.getGameId().equals(game1.getId())) {
                ArticleNotifications articleNotifications = watchedGame.getResourceNotifications().getArticleNotifications();

                assert(articleNotifications.getArticleIds().isEmpty());
                break;
            }
        }

        // Delete the user and game
        userService.deleteUser(userId);
        gameRepository.delete(game1);
    }

    @Test
    public void updateSettingsTest() throws JsonProcessingException {
        final String userId = "UserServiceTest - settingsUserTest";

        // Create the user
        userService.createUser(userId);
        // Get the user
        User user = userService.getUser(userId);

        boolean receiveNotifications;
        boolean receiveArticleNotifications;
        boolean notifyOnlyIfImportant;

        receiveNotifications = true;
        receiveArticleNotifications = false;
        notifyOnlyIfImportant = false;


        NotificationSettings notificationSettings = new NotificationSettings(receiveNotifications,  receiveArticleNotifications, notifyOnlyIfImportant);

        Settings settings = new Settings(notificationSettings);

        userService.updateSettings(userId, settings);

         user = userService.getUser(userId);
         assertThat(user.getSettings().getNotificationSettings().getReceiveNotifications(), is(receiveNotifications));
        assertThat(user.getSettings().getNotificationSettings().getReceiveArticleNotifications(), is(receiveArticleNotifications));
        assertThat(user.getSettings().getNotificationSettings().getNotifyOnlyIfImportant(), is(notifyOnlyIfImportant));

        ObjectMapper obj= new ObjectMapper();
        System.out.println(obj.writerWithDefaultPrettyPrinter().writeValueAsString(user));

        //Delete the user
       userService.deleteUser(userId);

        // Ensure that the user is deleted
        assertThat(userService.checkUserExists(userId), is(false));
    }


}

