package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import com.google.cloud.Timestamp;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.ContentPreferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationCategory;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.NotificationPreferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;
import java.util.Vector;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAndDelete() {
        Timestamp timestamp;
        Instant currentTime = timestamp.now();
        String timeInSeconds = Long.toString(currentTime.getEpochSecond());
        String firstName = "Jacob";
        String lastName = "Cook";
        String email = "jcook006@odu.edu";

        ContentPreferences contentPreferences = new ContentPreferences(false);
        List<String> resourceCategories = new Vector<>();
        String resourceCategory = "article";
        resourceCategories.add(resourceCategory);

        NotificationPreferences notificationPreferences = new NotificationPreferences(resourceCategories);
        Preferences preferences = new Preferences(contentPreferences, notificationPreferences);

        List<WatchedGame> watchList = new Vector<WatchedGame>();
        String gameId = "game" + timeInSeconds;

        String imageId = "image" + timeInSeconds;
        Image image = new Image(imageId, "logo", new Binary());
        List<Image> images = new Vector<>();
        images.add()
        Resources resources = new Resources()
        NotificationCategory notificationCategories = new NotificationCategory("article", 2, )
        WatchedGame watchedGame = new WatchedGame(gameId + "game", 2,  );

        String userId = "user" + timeInSeconds;

        // Create a user
        User user = new User(userId, firstName, lastName, email, UserStatus.active, UserPlan.free, null, null);

        // Save user in database
        userRepository.save(user);
        assert(userRepository.existsById(userId);

        // Delete user in database
        userRepository.delete(user);
        assert(!userRepository.existsById(userId));
    }
}