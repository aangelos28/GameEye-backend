package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.responses.WatchedGameResponse;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.WatchlistService;
import org.junit.jupiter.api.Test;
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

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WatchlistServiceTests {

    @Autowired
    private UserRepository users;

    @Autowired
    private GameRepository games;

    @Autowired
    private WatchlistService watchlistService;

    @Test
    public void testAddDeleteWatchlistGames() throws Exception {
        final String id = "WatchlistServiceTests-testAddWatchlistGames";

        // Delete user if present
        users.deleteById(id);

        // Add test user
        User user = new User();
        user.setId(id);
        users.save(user);

        // Add games to the user's watchlist
        final String game1Title = "Fallout 3";
        final String igdbId1 = "1";

        final String game2Title = "Fallout 2";
        final String igdbId2 = "2";

        final String game3Title = "Fallout: New Vegas";
        final String igdbId3 = "3";

        // Insert test games
        // Delete games if they exist
        games.deleteByTitle(game1Title);
        games.deleteByTitle(game2Title);
        games.deleteByTitle(game3Title);

        Game game1 = new Game();
        game1.setTitle(game1Title);
        game1.setIgdbId(igdbId1);
        game1.setReleaseDate(new Date());

        Game game2 = new Game();
        game2.setTitle(game2Title);
        game2.setIgdbId(igdbId2);
        game2.setReleaseDate(new Date());

        Game game3 = new Game();
        game3.setTitle(game3Title);
        game3.setIgdbId(igdbId3);
        game3.setReleaseDate(new Date());

        games.save(game1);
        games.save(game2);
        games.save(game3);

        game1 = games.findGameByTitle(game1Title);
        game2 = games.findGameByTitle(game2Title);
        game3 = games.findGameByTitle(game3Title);

        assertThat(game1.getWatchers(), is(0));
        assertThat(game2.getWatchers(), is(0));
        assertThat(game2.getWatchers(), is(0));

        watchlistService.addWatchlistGame(id, game1.getId());
        watchlistService.addWatchlistGame(id, game2.getId());
        watchlistService.addWatchlistGame(id, game3.getId());

        List<WatchedGameResponse> watchlist = watchlistService.getAllWatchlistGames(id);

        // Verify that games are present
        game1 = games.findGameByTitle(game1Title);
        game2 = games.findGameByTitle(game2Title);
        game3 = games.findGameByTitle(game3Title);

        assertThat(watchlist, hasItem(hasProperty("gameId", is(game1.getId()))));
        assertThat(watchlist, hasItem(hasProperty("gameId", is(game2.getId()))));
        assertThat(watchlist, hasItem(hasProperty("gameId", is(game3.getId()))));
        assertThat(game1.getWatchers(), is(1));
        assertThat(game2.getWatchers(), is(1));
        assertThat(game2.getWatchers(), is(1));

        // Delete watchlist games
        watchlistService.deleteWatchlistGameByIndex(id, 2);
        watchlistService.deleteWatchlistGameByIndex(id, 1);
        watchlistService.deleteWatchlistGameByIndex(id, 0);

        game1 = games.findGameByTitle(game1Title);
        game2 = games.findGameByTitle(game2Title);
        game3 = games.findGameByTitle(game3Title);
        assertThat(game1.getWatchers(), is(0));
        assertThat(game2.getWatchers(), is(0));
        assertThat(game3.getWatchers(), is(0));

        // Delete user
        users.deleteById(id);

        // Delete inserted games
        games.deleteByTitle(game1Title);
        games.deleteByTitle(game2Title);
        games.deleteByTitle(game3Title);
    }
}
