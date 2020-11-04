package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.CompanyResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    public void testGetCompanies() {
        List<CompanyResponse> companies = igdbService.getCompanies();
        assertThat(companies.size(), equalTo(10));
    }

    @Test
    public void testGetGameById() {
        int igdbId = 300;
        Game game = igdbService.getGameById(igdbId);

        assert(game.getIgdbId().equals(String.valueOf(igdbId)));
    }

    @Test
    public void testGetGamesByRangeWithLimit() {
        int minId = 1;
        int maxId = 10;
        int limit = 10;

        List<Game> games = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);

        assert(games.size()==10);
    }

    @Test
    public void testGetGamesByRange() {
        int minId = 1;
        int maxId = 100;
        int limit = 100;

        List<Game> games = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);
        assert(games.size()==100);
    }

    @Test
    public void testConvertGameResponsesToGames() {
        int minId = 1;
        int maxId = 100;
        int limit = 100;

        List<GameResponse> responses = igdbService.getGameResponsesWithMultipleRequests(minId, maxId, limit);
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
            List<String> responsePlatforms = responses.get(gameIndex).getPlatforms();
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
            List<String> responseGenres = responses.get(gameIndex).getGenres();
            List<String> gameGenres = games.get(gameIndex).getGenres();
            for (int genreIndex = 0; genreIndex < responseGenres.size(); genreIndex++) {
                String responseGenre = responseGenres.get(genreIndex);
                String gameGenre = gameGenres.get(genreIndex);
                assert(responseGenre.equals(gameGenre));
            }

            // Check sourceUrls
            SourceUrls responseUrls = responses.get(gameIndex).getSourceUrls();
            SourceUrls gameUrls = games.get(gameIndex).getSourceUrls();
            assert(responseUrls.equals(gameUrls));
        }
    }
}
