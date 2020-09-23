package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    User testUser = new User();

    @BeforeEach
    public void insertTestUserIntoGameEyeTest() throws IOException {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        ObjectMapper objectMapper = new ObjectMapper();

        String resourcesPath = "src/test/resources";
        String userJsonFileName = "user.json";
        String userFilePath = resourcesPath + File.separator + userJsonFileName;

        testUser = objectMapper.readValue(new File(userFilePath), User.class);
        userRepository.insert(testUser);

    }

    @AfterEach
    public void deleteUserFromGameEyeTest() {
        String userId = testUser.getId();
        if (userRepository.existsById(userId))
            userRepository.deleteById(userId);
    }

    @Test
    public void findUserByEmail() {
        User foundUser = userRepository.findUserByEmail(testUser.getEmail());

        assert(foundUser.getEmail().equals(testUser.getEmail()));

    }

    @Test
    public void findUsersWatchList() {
        User foundUser = userRepository.findUserByEmail(testUser.getEmail());

        List<WatchedGame> foundUserWatchList = foundUser.getWatchList();
        List<WatchedGame> testUserWatchList = testUser.getWatchList();

        for (int i = 0; i < foundUserWatchList.size(); i++) {
            String foundGameId = foundUserWatchList.get(i).getGameId();
            String testGameId =  testUserWatchList.get(i).getGameId();

            assert(foundGameId.equals(testGameId));
        }

    }

}