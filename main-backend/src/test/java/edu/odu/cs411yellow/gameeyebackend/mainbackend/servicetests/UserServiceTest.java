package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.UserStatus;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings.ArticleNotifications;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
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

        // Delete the user
        userService.deleteUser(userId);
    }
}

