package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.UserStatus;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.UserService;
import org.junit.Test;
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
}

