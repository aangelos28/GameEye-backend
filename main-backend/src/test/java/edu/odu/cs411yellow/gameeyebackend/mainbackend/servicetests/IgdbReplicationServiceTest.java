package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

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

            // Check sourceUrls. Urls that were empty can be empty or non-empty.
            // Non-empty urls should remain non-empty and not become empty.
            SourceUrls preRepSourceUrls = preRepGame.getSourceUrls();
            SourceUrls postRepSourceUrls = postRepGame.getSourceUrls();

            if (!preRepSourceUrls.getPublisherUrl().equals("")) {
                assertThat(postRepSourceUrls.getPublisherUrl(), notNullValue());
            }
            if (!preRepSourceUrls.getSteamUrl().equals("")) {
                assertThat(postRepSourceUrls.getSteamUrl(), notNullValue());
            }
            if (!preRepSourceUrls.getSubRedditUrl().equals("")) {
                assertThat(postRepSourceUrls.getSubRedditUrl(), notNullValue());
            }
            if (!preRepSourceUrls.getTwitterUrl().equals("")) {
                assertThat(postRepSourceUrls.getTwitterUrl(), notNullValue());
            }
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
        int minId = 1;
        int maxId = 10;
        int limit = 10;

        List<Game> igdbGames = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);
        List<Game> preReplicationTestDbGames = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        // Make list of titles from retrieved igdb games
        for (Game game: igdbGames) {
            titles.add(game.getTitle());
        }

        // Replicate new games to db.
        String status = igdbReplicationService.replicateGamesByTitles(titles, limit);

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
    public void testReplicateGamesByTitlesForUpdatingExistingGames() {
        // Test for adding new games
        int minId = 1;
        int maxId = 10;
        int limit = 10;

        List<Game> igdbGames = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);
        List<Game> preReplicationTestDbGames = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        // Make list of titles from retrieved igdb games
        for (Game game: igdbGames) {
            titles.add(game.getTitle());
        }

        // Replicate new games to db.
        String status = igdbReplicationService.replicateGamesByTitles(titles, limit);

        // Find new games from db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            preReplicationTestDbGames.add(gameRepository.findByIgdbId(String.valueOf(igdbId)));
        }

        // Test for updating existing games. Ensure only required fields are overwritten.
        List<Game> postReplicationTestDbGames = new ArrayList<>();
        igdbReplicationService.replicateGamesByTitles(titles, limit);

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

            // Check sourceUrls. Urls that were empty can be empty or non-empty.
            // Non-empty urls should remain non-empty and not become empty.
            SourceUrls preRepSourceUrls = preRepGame.getSourceUrls();
            SourceUrls postRepSourceUrls = postRepGame.getSourceUrls();

            if (!preRepSourceUrls.getPublisherUrl().equals("")) {
                assertThat(postRepSourceUrls.getPublisherUrl(), notNullValue());
            }
            if (!preRepSourceUrls.getSteamUrl().equals("")) {
                assertThat(postRepSourceUrls.getSteamUrl(), notNullValue());
            }
            if (!preRepSourceUrls.getSubRedditUrl().equals("")) {
                assertThat(postRepSourceUrls.getSubRedditUrl(), notNullValue());
            }
            if (!preRepSourceUrls.getTwitterUrl().equals("")) {
                assertThat(postRepSourceUrls.getTwitterUrl(), notNullValue());
            }
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
}
