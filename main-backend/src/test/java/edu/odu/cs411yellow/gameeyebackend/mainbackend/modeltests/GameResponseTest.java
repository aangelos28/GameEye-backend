package edu.odu.cs411yellow.gameeyebackend.mainbackend.modeltests;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class GameResponseTest {

    @Autowired
    IgdbService igdbService;

    @Autowired
    GameRepository gameRepository;

    @BeforeEach
    public void setup () {

    }

    @AfterEach
    public void tearDown () {

    }

    @Test
    public void testToGame () throws JsonProcessingException {
        String id = "1000";

        IgdbModel.GameResponse gameResponse = igdbService.getGameResponseById(id);

        Game game = gameResponse.toGame();

        // Insert game into GameEyeTest
        gameRepository.save(game);

    }

}
