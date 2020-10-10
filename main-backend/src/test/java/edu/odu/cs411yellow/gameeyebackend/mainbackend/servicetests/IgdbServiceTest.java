package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import com.fasterxml.jackson.core.JsonProcessingException;

import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GenreResponse;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
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

import java.util.ArrayList;
import java.util.Date;
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
    public void testGetGameById () throws JsonProcessingException {

        int igdbId = 300;
        Game game = igdbService.getGameById(igdbId);

        assert(game.getIgdbId().equals(String.valueOf(igdbId)));

    }

    @Test
    public void testGetGenresFromGenreResponse () throws JsonProcessingException {
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

        List<String> genres = gameResponse.getGenresFromGenreResponses();

        for(int i = 0; i < gameResponse.genres.size(); i++) {
            String genreFromGameResponse = gameResponse.genres.get(i).name;
            String genreFromGenres = genres.get(i);

            assert(genreFromGameResponse.equals(genreFromGenres));

        }

    }

    @Test
    public void testGetGamesByRangeWithLimit() throws JsonProcessingException {
        int minId = 1;
        int maxId = 250;
        int limit = 250;

        List<Game> games = igdbService.getGamesByRangeWithLimit(minId, maxId, limit);

        for (Game game: games) {
            if (!gameRepository.existsByIgdbId(game.getIgdbId()) && game.getIgdbId()!= "") {
                gameRepository.save(game);
            }
        }

    }

    @Test
    public void testGetGamesByRange() throws JsonProcessingException {
        int minId = 1;
        int maxId = 1000;

        List<Game> games = igdbService.getGamesByRange(minId, maxId);

        for (Game game: games) {
            System.out.println(game.getIgdbId() + ": " + game.getTitle() + "\n");
        };
    }

    @Test
    public void testConvertGameResponsesToGames() throws JsonProcessingException {
        int minId = 1;
        int maxId = 100;

        List<GameResponse> responses = igdbService.getGameResponsesByRange(minId, maxId);
        List<Game> games = igdbService.convertGameResponsesToGames(responses);

        for (int gameIndex = 0; gameIndex < games.size(); gameIndex++) {
            // Check igdbId
            String responseIgdbId = responses.get(gameIndex).igdbId;
            String gameIgdbId = games.get(gameIndex).getIgdbId();
            assert(responseIgdbId.equals(gameIgdbId));

            // Check title
            String responseTitle = responses.get(gameIndex).title;
            String gameTitle = games.get(gameIndex).getTitle();
            assert(responseTitle.equals(gameTitle));

            // Check platforms
            List<String> responsePlatforms = responses.get(gameIndex).getPlatformsFromPlatformResponses();
            List<String> gamePlatforms = games.get(gameIndex).getPlatforms();
            for (int platformIndex = 0; platformIndex < responsePlatforms.size(); platformIndex++) {
                String responsePlatform = responsePlatforms.get(platformIndex);
                String gamePlatform = gamePlatforms.get(platformIndex);
                assert(responsePlatform.equals(gamePlatform));
            }

            // Check lastUpdated
            int responseLastUpdatedInSeconds = responses.get(gameIndex).lastUpdatedInSeconds;
            // Convert UNIX epoch timestamp from IGDB to year, month, day format
            Date responseLastUpdated = new java.util.Date((long)responseLastUpdatedInSeconds*1000);
            Date gameLastUpdated = games.get(gameIndex).getLastUpdated();
            assert(responseLastUpdated.equals(gameLastUpdated));

            // Check genres
            List<String> responseGenres = responses.get(gameIndex).getGenresFromGenreResponses();
            List<String> gameGenres = games.get(gameIndex).getGenres();
            for (int genreIndex = 0; genreIndex < responseGenres.size(); genreIndex++) {
                String responseGenre = responseGenres.get(genreIndex);
                String gameGenre = gameGenres.get(genreIndex);
                assert(responseGenre.equals(gameGenre));
            }

            // Check sourceUrls
            SourceUrls responseUrls = responses.get(gameIndex).getSourceUrlsFromWebsiteResponses();
            SourceUrls gameUrls = games.get(gameIndex).getSourceUrls();
            assert(responseUrls.equals(gameUrls));

        }

    }

}
