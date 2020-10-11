package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbReplicatorService;
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
public class IgdbReplicatorServiceTest {
    @Autowired
    IgdbReplicatorService igdbReplicator;

    @Autowired
    IgdbService igdbService;

    @Autowired
    GameRepository gameRepository;

    @Test
    public void testReplicateIgdbByRangeForNewGames() throws JsonProcessingException {
        // Test for adding new games
        int minId = 1;
        int maxId = 10;
        int limit = 10;

        List<Game> igdbGames = igdbService.getGamesByRange(minId, maxId, limit);
        List<Game> preReplicationTestDbGames = new ArrayList<>();

        // Replicate new games to db.
        igdbReplicator.replicateIgdbByRange(minId, maxId, limit);

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

        // Delete new games written to db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            gameRepository.deleteByIgdbId(String.valueOf(igdbId));
        }
    }

    @Test
    public void testReplicateIgdbByRangeForUpdatingExistingGames() throws JsonProcessingException {
        // Test for adding new games
        int minId = 1;
        int maxId = 10;
        int limit = 10;

        List<Game> igdbGames = igdbService.getGamesByRange(minId, maxId, limit);
        List<Game> preReplicationTestDbGames = new ArrayList<>();

        // Replicate new games to db.
        igdbReplicator.replicateIgdbByRange(minId, maxId, limit);

        // Find new games from db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            preReplicationTestDbGames.add(gameRepository.findByIgdbId(String.valueOf(igdbId)));
        }

        // Test for updating existing games. Ensure only required fields are overwritten.
        List<Game> postReplicationTestDbGames = new ArrayList<>();
        igdbReplicator.replicateIgdbByRange(minId, maxId, limit);

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
            assertThat(preRepGame.getStatus(), equalTo(postRepGame.getStatus()));
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

        // Delete new and updated games written to db.
        for (int igdbId = minId; igdbId < maxId-minId+2; igdbId++) {
            gameRepository.deleteByIgdbId(String.valueOf(igdbId));
        }
    }
}
