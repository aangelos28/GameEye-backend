package edu.odu.cs411yellow.gameeyebackend.mainbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class IgdbServiceTest {

    @Autowired
    IgdbService igdbService;

    @Autowired
    GameRepository gameRepository;

    @BeforeEach
    public void setup () {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetCompanies () {

        String igdbOutput = igdbService.getCompanies();

    }

    @Test
    public void testGetGameResponseById () throws JsonProcessingException {

        String id = "100";
        IgdbModel.GameResponse gameResponse = igdbService.getGameResponseById(id);

        assert(gameResponse.id.equals(id));

    }

    @Test
    public void testGetGenresFromGenreResponse () throws JsonProcessingException {

        String id = "100";
        IgdbModel.GameResponse gameResponse = igdbService.getGameResponseById(id);

        List<String> genres = gameResponse.getGenresFromGenreResponses();

        for(int i = 0; i < gameResponse.genreResponses.size(); i++) {
            String genreFromGameResponse = gameResponse.genreResponses.get(i).name;
            String genreFromGenres = genres.get(i);

            assert(genreFromGameResponse.equals(genreFromGenres));

        }

    }

    @Test
    public void testGetGameResponsesByRange() throws JsonProcessingException {
        int lowerId = 1;
        int upperId = 100;

        List<IgdbModel.GameResponse> gameResponses = igdbService.getGameResponsesByRange(lowerId, upperId);

        for (IgdbModel.GameResponse gameResponse: gameResponses) {
            gameRepository.save(gameResponse.toGame());

        }

    }
}
