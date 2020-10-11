package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.ProfileController;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ProfileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import static org.junit.Assert.assertEquals;
import org.springframework.test.context.junit4.SpringRunner;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestProfileService {
    @Autowired UserRepository users;

    @Autowired ProfileService profileService;

/*
    @Test
    public void TestProfileConstructor(){
        final String firebaseID = "Profile Constructor Test";
        User user = new User();
        user.setFirebaseId(firebaseID);
        users.save(user);

        ProfileService profileService = new ProfileService(users);


    }
    */

    @Test
    public void TestRegisterUser()
    {
        final String firebaseID = "Profile Service Test";

        User user = new User();
        user.setFirebaseId(firebaseID);
        users.save(user);

        profileService.registerUser(firebaseID);

        assertEquals("profile Service Test", user.getFirebaseId());
        users.delete(user);
    }


}

