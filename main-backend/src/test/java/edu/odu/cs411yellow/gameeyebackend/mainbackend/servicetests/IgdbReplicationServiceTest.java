package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbReplicationService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class IgdbReplicationServiceTest {
    @Autowired
    IgdbReplicationService igdbReplicationService;

    @Autowired
    IgdbService igdbService;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    ElasticGameRepository elasticRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testReplicateIgdbByRangeForNewGames() {
        // Test for adding new games
        int minId = 1;
        int maxId = 10;
        int limit = 10;

        List<Game> igdbGames = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);
        List<Game> preReplicationTestDbGames = new ArrayList<>();

        // Replicate new games to db.
        String result = igdbReplicationService.replicateGamesByRange(minId, maxId, limit);

        // Find new games from db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            preReplicationTestDbGames.add(gameRepository.findByIgdbId(String.valueOf(igdbId)));
        }

        // Compare IGDB games to new games written to db.
        for (int igdbId = 0; igdbId < igdbGames.size(); igdbId++) {
            // Check igdbId
            String igdbGameId = igdbGames.get(igdbId).getIgdbId();
            String preRepGameId = preReplicationTestDbGames.get(igdbId).getIgdbId();
            assertThat(igdbGameId, equalTo(preRepGameId));

            // Check platforms
            List<String> igdbPlatforms = igdbGames.get(igdbId).getPlatforms();
            List<String> preRepPlatforms = preReplicationTestDbGames.get(igdbId).getPlatforms();
            assertThat(igdbPlatforms, equalTo(igdbPlatforms));

            // Check genres
            List<String> igdbGenres = igdbGames.get(igdbId).getGenres();
            List<String> preRepGenres = igdbGames.get(igdbId).getGenres();
            assertThat(igdbGenres, equalTo(preRepGenres));

            // Check sourceUrls
            SourceUrls igdbSourceUrls = igdbGames.get(igdbId).getSourceUrls();
            SourceUrls preRepSourceUrls = preReplicationTestDbGames.get(igdbId).getSourceUrls();
            assertThat(igdbSourceUrls, equalTo(preRepSourceUrls));
        }

        // Delete new games from elastic.
        for (int currentId = minId; currentId < maxId + 1; currentId++) {
            if (gameRepository.existsByIgdbId(String.valueOf(currentId))) {
                String gameId = gameRepository.findByIgdbId(String.valueOf(currentId)).getId();
                elasticRepository.deleteByGameId(gameId);
            }
        }

        // Delete new games from mongo db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            gameRepository.deleteByIgdbId(String.valueOf(igdbId));
        }
    }

    @Test
    public void testReplicateIgdbByRangeForUpdatingExistingGames() {
        // Test for adding new games
        int minId = 1;
        int maxId = 10;
        int limit = 10;

        List<Game> igdbGames = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);
        List<Game> preReplicationTestDbGames = new ArrayList<>();

        // Replicate new games to db.
        String status = igdbReplicationService.replicateGamesByRange(minId, maxId, limit);

        // Find new games from db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            preReplicationTestDbGames.add(gameRepository.findByIgdbId(String.valueOf(igdbId)));
        }

        // Test for updating existing games. Ensure only required fields are overwritten.
        List<Game> postReplicationTestDbGames = new ArrayList<>();
        igdbReplicationService.replicateGamesByRange(minId, maxId, limit);

        // Find updated games in db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            postReplicationTestDbGames.add(gameRepository.findByIgdbId(String.valueOf(igdbId)));
        }

        // Compare IGDB games to new games written to db.
        for (int igdbId = 0; igdbId < igdbGames.size(); igdbId++) {
            Game preRepGame = preReplicationTestDbGames.get(igdbId);
            Game postRepGame = postReplicationTestDbGames.get(igdbId);

            // The following fields should remain the same before/after replication
            assertThat(preRepGame.getId(), equalTo(postRepGame.getId()));
            assertThat(preRepGame.getIgdbId(), equalTo(postRepGame.getIgdbId()));
            assertThat(preRepGame.getTitle(), equalTo(postRepGame.getTitle()));
            assertThat(preRepGame.getReleaseDate(), equalTo(postRepGame.getReleaseDate()));
            assertThat(preRepGame.getResources(), equalTo(postRepGame.getResources()));
            assertThat(preRepGame.getWatchers(), equalTo(postRepGame.getWatchers()));
        }

        // Delete new games from elastic.
        for (int currentId = minId; currentId < maxId + 1; currentId++) {
            if (gameRepository.existsByIgdbId(String.valueOf(currentId))) {
                String gameId = gameRepository.findByIgdbId(String.valueOf(currentId)).getId();
                elasticRepository.deleteByGameId(gameId);
            }
        }

        // Delete new and updated games written to db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            gameRepository.deleteByIgdbId(String.valueOf(igdbId));
        }
    }

    @Test
    public void testReplicateIgdbByTitlesForNewGames() {
        // Test for adding new games
        // List of ids for games titles with only one id match
        int igdbId1 = 178;  // "Battlehawks 1942"
        int igdbId2 = 156;  // "Star Wars: Episode I - Battle for Naboo"
        int igdbId3 = 189;  // "LEGO Indiana Jones: The Original Adventures"
        int limit = 10;

        List<Game> igdbGames = new ArrayList<>();
        igdbGames.add(igdbService.retrieveGameById(igdbId1));
        igdbGames.add(igdbService.retrieveGameById(igdbId2));
        igdbGames.add(igdbService.retrieveGameById(igdbId3));

        List<Game> preReplicationTestDbGames = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        // Fill titles list with game titles from retrieved IGDB games
        for (Game game: igdbGames) {
            titles.add(game.getTitle());
        }

        // Replicate new games to db.
        String result = igdbReplicationService.replicateGamesByTitles(titles, limit);

        // Find new games from db.
        for (Game game: igdbGames) {
            preReplicationTestDbGames.add(gameRepository.findByIgdbId(String.valueOf(game.getIgdbId())));
        }

        // Compare IGDB games to new games written to db.
        for (int i = 0; i < igdbGames.size(); i++) {
            // Check igdbId
            String igdbGameId = igdbGames.get(i).getIgdbId();
            String preRepGameId = preReplicationTestDbGames.get(i).getIgdbId();
            assertThat(igdbGameId, equalTo(preRepGameId));

            // Check platforms
            List<String> igdbPlatforms = igdbGames.get(i).getPlatforms();
            List<String> preRepPlatforms = preReplicationTestDbGames.get(i).getPlatforms();
            assertThat(igdbPlatforms, equalTo(igdbPlatforms));

            // Check genres
            List<String> igdbGenres = igdbGames.get(i).getGenres();
            List<String> preRepGenres = igdbGames.get(i).getGenres();
            assertThat(igdbGenres, equalTo(preRepGenres));

            // Check sourceUrls
            SourceUrls igdbSourceUrls = igdbGames.get(i).getSourceUrls();
            SourceUrls preRepSourceUrls = preReplicationTestDbGames.get(i).getSourceUrls();
            assertThat(igdbSourceUrls, equalTo(preRepSourceUrls));
        }

        // Delete new games from elastic.
        for (Game game: preReplicationTestDbGames) {
            if (gameRepository.existsByIgdbId(String.valueOf(game.getIgdbId()))) {
                String gameId = gameRepository.findByIgdbId(String.valueOf(game.getIgdbId())).getId();
                elasticRepository.deleteByGameId(gameId);
            }
        }

        // Delete new games from mongo db.
        for (Game game: preReplicationTestDbGames) {
            gameRepository.deleteByIgdbId(String.valueOf(game.getIgdbId()));
        }
    }

    @Test
    public void testReplicateIgdbByTitlesForUpdatingExistingGames() {
        // Test for adding new games
        // List of ids for games titles with only one id match
        int igdbId1 = 178;  // "Battlehawks 1942"
        int igdbId2 = 156;  // "Star Wars: Episode I - Battle for Naboo"
        int igdbId3 = 189;  // "LEGO Indiana Jones: The Original Adventures"
        int limit = 10;

        List<Game> igdbGames = new ArrayList<>();
        igdbGames.add(igdbService.retrieveGameById(igdbId1));
        igdbGames.add(igdbService.retrieveGameById(igdbId2));
        igdbGames.add(igdbService.retrieveGameById(igdbId3));

        List<Game> preReplicationTestDbGames = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        // Fill titles list with game titles from retrieved IGDB games
        for (Game game: igdbGames) {
            titles.add(game.getTitle());
        }

        // Replicate new games to db.
        String result1 = igdbReplicationService.replicateGamesByTitles(titles, limit);

        // Find new games from db.
        for (Game game: igdbGames) {
            preReplicationTestDbGames.add(gameRepository.findByIgdbId(String.valueOf(game.getIgdbId())));
        }

        // Test for updating existing games. Ensure only required fields are overwritten.
        List<Game> postReplicationTestDbGames = new ArrayList<>();
        String result2 = igdbReplicationService.replicateGamesByTitles(titles, limit);

        // Find updated games in db.
        for (Game game: igdbGames) {
            postReplicationTestDbGames.add(gameRepository.findByIgdbId(String.valueOf(game.getIgdbId())));
        }

        // Compare IGDB games to new games written to db.
        for (int i = 0; i < igdbGames.size(); i++) {
            Game preRepGame = preReplicationTestDbGames.get(i);
            Game postRepGame = postReplicationTestDbGames.get(i);

            // The following fields should remain the same before/after replication
            assertThat(preRepGame.getId(), equalTo(postRepGame.getId()));
            assertThat(preRepGame.getIgdbId(), equalTo(postRepGame.getIgdbId()));
            assertThat(preRepGame.getTitle(), equalTo(postRepGame.getTitle()));
            assertThat(preRepGame.getReleaseDate(), equalTo(postRepGame.getReleaseDate()));
            assertThat(preRepGame.getResources(), equalTo(postRepGame.getResources()));
            assertThat(preRepGame.getWatchers(), equalTo(postRepGame.getWatchers()));
        }

        // Delete new games from elastic.
        for (Game game: preReplicationTestDbGames) {
            if (gameRepository.existsByIgdbId(String.valueOf(game.getIgdbId()))) {
                String gameId = gameRepository.findByIgdbId(String.valueOf(game.getIgdbId())).getId();
                elasticRepository.deleteByGameId(gameId);
            }
        }

        // Delete new and updated games written to db.
        for (Game game: preReplicationTestDbGames) {
            gameRepository.deleteByIgdbId(String.valueOf(game.getIgdbId()));
        }
    }

    @Test
    public void testReplicateNewReleasesForUpdatingExistingGames() throws JsonProcessingException {
        int numNewGames = 100;
        int limit = 250;

        // Replicate new games to db.
        String result1 = igdbReplicationService.replicateNewReleases(numNewGames, limit);

        // Find new games from db.
        List<Game> preReplicationTestDbGames = gameRepository.findAll();

        // Test for updating existing games. Ensure only required fields are overwritten.
        String result2 = igdbReplicationService.replicateNewReleases(numNewGames, limit);

        // Find updated games in db.
        List<Game> postReplicationTestDbGames = gameRepository.findAll();

        // Compare IGDB games to new games written to db.
        for (int i = 0; i < preReplicationTestDbGames.size(); i++) {
            Game preRepGame = preReplicationTestDbGames.get(i);
            Game postRepGame = postReplicationTestDbGames.get(i);

            // The following fields should remain the same before/after replication
            assertThat(preRepGame.getId(), equalTo(postRepGame.getId()));
            assertThat(preRepGame.getIgdbId(), equalTo(postRepGame.getIgdbId()));
            assertThat(preRepGame.getTitle(), equalTo(postRepGame.getTitle()));
            assertThat(preRepGame.getReleaseDate(), equalTo(postRepGame.getReleaseDate()));
            assertThat(preRepGame.getResources(), equalTo(postRepGame.getResources()));
            assertThat(preRepGame.getWatchers(), equalTo(postRepGame.getWatchers()));

            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(postRepGame));
        }

        // Delete new games from elastic.
        for (Game game: preReplicationTestDbGames) {
            if (gameRepository.existsByIgdbId(String.valueOf(game.getIgdbId()))) {
                String gameId = gameRepository.findByIgdbId(String.valueOf(game.getIgdbId())).getId();
                elasticRepository.deleteByGameId(gameId);
            }
        }

        // Delete new and updated games written to db.
        for (Game game: preReplicationTestDbGames) {
            gameRepository.deleteByIgdbId(String.valueOf(game.getIgdbId()));
        }
    }
}
