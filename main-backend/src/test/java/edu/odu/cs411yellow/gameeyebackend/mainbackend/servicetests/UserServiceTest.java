package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Settings;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.UserStatus;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings.NotificationSettings;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;

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
    public void updateSettingsTest() throws JsonProcessingException {
        final String userId = "UserServiceTest - settingsUserTest";

        // Create the user
        userService.createUser(userId);
        // Get the user
        User user = userService.getUser(userId);

        boolean showArticles;
        boolean showImages;
        boolean notifyOnlyIfImportant;

        showArticles = true;
        showImages = false;
        notifyOnlyIfImportant = false;


        NotificationSettings notificationSettings = new NotificationSettings(showArticles, notifyOnlyIfImportant);

        Settings settings = new Settings(notificationSettings);

        userService.updateSettings(userId, settings);

         user = userService.getUser(userId);

        assertThat(user.getSettings().getNotificationSettings().getReceiveArticleNotifications(), is(showArticles));
        assertThat(user.getSettings().getNotificationSettings().getNotifyOnlyIfImportant(), is(notifyOnlyIfImportant));

        ObjectMapper obj= new ObjectMapper();
        System.out.println(obj.writerWithDefaultPrettyPrinter().writeValueAsString(user));

        //Delete the user
       userService.deleteUser(userId);

        // Ensure that the user is deleted
        assertThat(userService.checkUserExists(userId), is(false));
    }


}

