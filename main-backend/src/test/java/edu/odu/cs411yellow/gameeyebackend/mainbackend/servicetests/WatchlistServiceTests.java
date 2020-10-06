package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.WatchlistService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WatchlistServiceTests {

    @Autowired
    private UserRepository users;

    @Autowired
    private WatchlistService watchlistService;

    @Test
    public void testAddWatchlistGames() {
        final String userFirebaseId = "WatchlistServiceTests-testAddWatchlistGames";

        // Delete user if present
        users.deleteByFirebaseId(userFirebaseId);

        // Add test user
        User user = new User();
        user.setFirebaseId(userFirebaseId);
        users.save(user);

        // Add games to the user's watchlist
        final String game1Id = "5f78f69e7588682adc444b04";
        final String game2Id = "5f78f69e7588682adc444b08";
        final String game3Id = "5f78f69d7588682adc444aff";
        watchlistService.addWatchlistGame(userFirebaseId, game1Id);
        watchlistService.addWatchlistGame(userFirebaseId, game2Id);
        watchlistService.addWatchlistGame(userFirebaseId, game3Id);

        List<WatchedGame> watchlist = watchlistService.getWatchlistGames(userFirebaseId);

        // Verify that games are present
        assertThat(watchlist, hasItem(hasProperty("gameId", is(game1Id))));
        assertThat(watchlist, hasItem(hasProperty("gameId", is(game2Id))));
        assertThat(watchlist, hasItem(hasProperty("gameId", is(game3Id))));

        // Delete user
        users.deleteByFirebaseId(userFirebaseId);
    }

    @Test
    public void testDeleteWatchlistGames() throws Exception {
        final String userFirebaseId = "WatchlistServiceTests-testDeleteWatchlistGames";

        // Delete user if present
        users.deleteByFirebaseId(userFirebaseId);

        // Add test user
        User user = new User();
        user.setFirebaseId(userFirebaseId);
        users.save(user);

        // Add games to the user's watchlist
        final String game1Id = "5f78f69e7588682adc444b04";
        final String game2Id = "5f78f69e7588682adc444b08";
        final String game3Id = "5f78f69d7588682adc444aff";
        watchlistService.addWatchlistGame(userFirebaseId, game1Id);
        watchlistService.addWatchlistGame(userFirebaseId, game2Id);
        watchlistService.addWatchlistGame(userFirebaseId, game3Id);

        // Delete watchlist games
        watchlistService.deleteWatchlistGame(userFirebaseId, 2);
        watchlistService.deleteWatchlistGame(userFirebaseId, 1);
        watchlistService.deleteWatchlistGame(userFirebaseId, 0);

        List<WatchedGame> watchlist = watchlistService.getWatchlistGames(userFirebaseId);

        assertThat(watchlist, not(hasItem(hasProperty("gameId", is(game1Id)))));
        assertThat(watchlist, not(hasItem(hasProperty("gameId", is(game2Id)))));
        assertThat(watchlist, not(hasItem(hasProperty("gameId", is(game3Id)))));

        // Delete user
        users.deleteByFirebaseId(userFirebaseId);
    }
}
