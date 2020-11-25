package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class IgdbServiceTest {
    @Autowired
    IgdbService igdbService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetGameById() {
        int igdbId = 300;
        Game game = igdbService.retrieveGameById(igdbId);

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

        List<GameResponse> responses = igdbService.retrieveGameResponsesByIdRange(minId, maxId, limit);
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
            long responseLastUpdatedInSeconds = responses.get(gameIndex).lastUpdatedInSeconds;
            // Convert UNIX epoch timestamp from IGDB to year, month, day format
            Date responseLastUpdated = new java.util.Date(responseLastUpdatedInSeconds*1000);
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

    @Test
    public void testFindMaxId() throws JsonProcessingException {
        int maxId = igdbService.findMaxGameId();

        Game maxIdGame = new Game(igdbService.retrieveGameResponseById(maxId));
        assertThat(maxIdGame.getIgdbId(), is(not("")));

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(maxIdGame));
    }

    @Test
    public void testConvertTitlesToIgdbWhereClauseNames() {
        String title1 = "Call of Duty: Black Ops Cold War";
        String title2 = "Breath of the Wild";
        String title3 = "Hyrule Warriors";

        List<String> titles = new ArrayList<>(Arrays.asList(title1, title2, title3));

        String actualNames = igdbService.convertTitlesToIgdbWhereClauseNames(titles);
        String expectedNames = "(\"Call of Duty: Black Ops Cold War\", \"Breath of the Wild\", \"Hyrule Warriors\")";

        assertThat(actualNames, is(expectedNames));
        System.out.println(actualNames);
    }
}
