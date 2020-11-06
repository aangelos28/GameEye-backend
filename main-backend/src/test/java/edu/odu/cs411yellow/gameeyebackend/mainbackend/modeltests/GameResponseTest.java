package edu.odu.cs411yellow.gameeyebackend.mainbackend.modeltests;

import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GenreResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class GameResponseTest {

    @Autowired
    IgdbService igdbService;

    @Autowired
    GameRepository gameRepository;

    @Test
    public void testGetGenresFromGenreResponse () {
        String genreId1 = "1";
        String category1 = "First-person shooter";
        GenreResponse genre1 = new GenreResponse(genreId1, category1);

        String genreId2 = "2";
        String category2 = "Puzzle";
        GenreResponse genre2 = new GenreResponse(genreId2, category2);

        String genreId3 = "3";
        String category3 = "Adventure";
        GenreResponse genre3 = new GenreResponse(genreId3, category3);

        List<GenreResponse> genreResponses = new ArrayList<>();
        genreResponses.add(genre1);
        genreResponses.add(genre2);
        genreResponses.add(genre3);

        GameResponse gameResponse = new GameResponse();
        gameResponse.genres = genreResponses;

        List<String> genres = gameResponse.getGenres();

        for (int i = 0; i < gameResponse.genres.size(); i++) {
            String genreFromGameResponse = gameResponse.genres.get(i).name;
            String genreFromGenres = genres.get(i);

            assert(genreFromGameResponse.equals(genreFromGenres));
        }
    }
}
