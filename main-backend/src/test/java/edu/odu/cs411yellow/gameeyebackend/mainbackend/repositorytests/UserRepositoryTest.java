package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ContentPreferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ArticlesNotificationCategory;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationCategories;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationPreferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import org.bson.types.ObjectId;
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
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void saveAndDelete() {
        // Declare parameters for user
        String firstName = "Jacob";
        String lastName = "Cook";
        String email = "jcook006@odu.edu";

        // Declare parameters for preferences in User constructor
        ContentPreferences contentPreferences = new ContentPreferences(false);
        List<String> resourceCategories = new ArrayList<>();
        String resourceCategory = "article";
        resourceCategories.add(resourceCategory);

        NotificationPreferences notificationPreferences = new NotificationPreferences(resourceCategories);

        Preferences preferences = new Preferences(contentPreferences, notificationPreferences);

        // Declare parameters for watchList in User constructor
        String referencedGameName = "Doom Eternal";
        Game doomEternal = gameRepository.findGameByTitle(referencedGameName);
        Integer notificationCategoryCount = 2;

        List<String> resources = new ArrayList<>();

        ArticlesNotificationCategory articles =
                                     new ArticlesNotificationCategory(notificationCategoryCount, resources);

        NotificationCategories notificationCategories = new NotificationCategories(articles);

        WatchedGame watchedGame = new WatchedGame(doomEternal.getId(), notificationCategoryCount, notificationCategories);

        List<WatchedGame> watchList = new ArrayList<>();
        watchList.add(watchedGame);

        String userId = (new ObjectId()).toString();

        // Create a user
        User user = new User(userId, firstName, lastName, email, UserStatus.active,
                             UserPlan.free, preferences, watchList);

        // Save user in database
        userRepository.save(user);
        assert(userRepository.existsById(user.getId()));

        // Delete user in database
        userRepository.delete(user);
        assert(!userRepository.existsById(user.getId()));
    }

    @Test
    public void queryByEmail() {
        // Check against mock data in database
        String userEmail = "jcook006@odu.edu";

        User user = userRepository.findUserByEmail(userEmail);
        assert(user.getEmail().equals(userEmail));

        List<WatchedGame> watchList = user.getWatchList();
        WatchedGame watchedGame = watchList.get(0);

        String watchedGameId = watchedGame.getGameId();

        Game game = gameRepository.findGameById(watchedGameId);

        String expectedGameTitle = "Doom Eternal";
        String actualGameTitle = game.getTitle();

        assert(expectedGameTitle.equals(actualGameTitle));

        NotificationCategories notificationCategories = watchedGame.getNotificationCategories();
        ArticlesNotificationCategory articles = notificationCategories.getArticles();

        List<String> articleResourceIds = articles.getResourceIds();

        List<Article> foundArticles = game.getResources().getArticlesByIds(articleResourceIds);

        Article foundArticle = foundArticles.get(0);
        String actualArticleTitle = foundArticle.getTitle();
        String expectedArticleTitle = "Doom Eternal Single-Player Review";

        assert(actualArticleTitle.equals(expectedArticleTitle));
    }
}