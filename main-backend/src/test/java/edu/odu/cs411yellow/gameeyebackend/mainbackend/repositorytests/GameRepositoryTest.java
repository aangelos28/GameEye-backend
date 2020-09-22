package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
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
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void saveAndDelete() {
        // Create a game
        Game game = new Game("1234", "Zelda", null, null, null, null, null, null);

        // Save game in database
        gameRepository.save(game);
        assert(gameRepository.existsById("1234"));

        Game game1 = gameRepository.findGameByTitle("Doom Eternal");
        String title = game1.getTitle();

        // Delete game in database
        gameRepository.delete(game);
        assert(!gameRepository.existsById("1234"));
    }
}