package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import com.mongodb.DBRef;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ContentPreferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationCategory;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationPreferences;
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
        Game doomEternal = gameRepository.findGameByTitle("Doom Eternal");
        String notificationCategoryType = "article";
        Integer notificationCategoryCount = 2;

        List<ObjectId> resources = new ArrayList<>();

        ObjectId resourceId1 = new ObjectId();
        ObjectId resourceId2 = new ObjectId();

        resources.add(resourceId1);
        resources.add(resourceId2);

        NotificationCategory notificationCategory = new NotificationCategory( notificationCategoryType,
                                                                              notificationCategoryCount,
                                                                              resources );

        List<NotificationCategory> notificationCategories = new ArrayList<>();
        notificationCategories.add(notificationCategory);

        WatchedGame watchedGame = new WatchedGame(doomEternal, notificationCategoryCount, notificationCategories);

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
}