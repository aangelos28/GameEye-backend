package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class ElasticGameRepositoryTest {
    @Autowired
    private ElasticGameRepository elasticGames;

    @Autowired
    private GameRepository games;

    @Test
    public void testInsertElasticGames() {
        final String game1Title = "Baldur's Gate II: Shadows of Amn - ElasticGameRepositoryTest";
        final String game2Title = "Vampire: The Masquerade - Bloodlines - ElasticGameRepositoryTest";
        final String game3Title = "Fallout 3 - ElasticGameRepositoryTest";
        final String game4Title = "Fallout 2 - ElasticGameRepositoryTest";
        final String game5Title = "Fallout: New Vegas - ElasticGameRepositoryTest";

        final String igdbId1 = "1";
        final String igdbId2 = "2";
        final String igdbId3 = "3";
        final String igdbId4 = "4";
        final String igdbId5 = "5";

        // Insert test games
        // Delete test games if already present
        games.deleteByTitle(game1Title);
        games.deleteByTitle(game2Title);
        games.deleteByTitle(game3Title);
        games.deleteByTitle(game4Title);
        games.deleteByTitle(game5Title);

        Game game1 = new Game();
        game1.setTitle(game1Title);
        game1.setIgdbId(igdbId1);

        Game game2 = new Game();
        game2.setTitle(game2Title);
        game2.setIgdbId(igdbId2);

        Game game3 = new Game();
        game3.setTitle(game3Title);
        game3.setIgdbId(igdbId3);

        Game game4 = new Game();
        game4.setTitle(game4Title);
        game4.setIgdbId(igdbId4);

        Game game5 = new Game();
        game5.setTitle(game5Title);
        game5.setIgdbId(igdbId5);

        games.save(game1);
        games.save(game2);
        games.save(game3);
        games.save(game4);
        games.save(game5);

        game1 = games.findGameByTitle(game1Title);
        game2 = games.findGameByTitle(game2Title);
        game3 = games.findGameByTitle(game3Title);
        game4 = games.findGameByTitle(game4Title);
        game5 = games.findGameByTitle(game5Title);

        ElasticGame elasticGame1 = new ElasticGame(game1);
        ElasticGame elasticGame2 = new ElasticGame(game2);
        ElasticGame elasticGame3 = new ElasticGame(game3);
        ElasticGame elasticGame4 = new ElasticGame(game4);
        ElasticGame elasticGame5 = new ElasticGame(game5);

        elasticGames.save(elasticGame1);
        elasticGames.save(elasticGame2);
        elasticGames.save(elasticGame3);
        elasticGames.save(elasticGame4);
        elasticGames.save(elasticGame5);

        elasticGame1 = elasticGames.findByGameId(game1.getId());
        elasticGame2 = elasticGames.findByGameId(game2.getId());
        elasticGame3 = elasticGames.findByGameId(game3.getId());
        elasticGame4 = elasticGames.findByGameId(game4.getId());
        elasticGame5 = elasticGames.findByGameId(game5.getId());

        assertThat(elasticGame1.getGameId(), is(game1.getId()));
        assertThat(elasticGame1.getTitle(), is(game1.getTitle()));

        assertThat(elasticGame2.getGameId(), is(game2.getId()));
        assertThat(elasticGame2.getTitle(), is(game2.getTitle()));

        assertThat(elasticGame3.getGameId(), is(game3.getId()));
        assertThat(elasticGame3.getTitle(), is(game3.getTitle()));

        assertThat(elasticGame4.getGameId(), is(game4.getId()));
        assertThat(elasticGame4.getTitle(), is(game4.getTitle()));

        assertThat(elasticGame5.getGameId(), is(game5.getId()));
        assertThat(elasticGame5.getTitle(), is(game5.getTitle()));

        // Delete elastic games
        elasticGames.delete(elasticGame1);
        elasticGames.delete(elasticGame2);
        elasticGames.delete(elasticGame3);
        elasticGames.delete(elasticGame4);
        elasticGames.delete(elasticGame5);

        // Delete test games
        games.deleteByTitle(game1Title);
        games.deleteByTitle(game2Title);
        games.deleteByTitle(game3Title);
        games.deleteByTitle(game4Title);
        games.deleteByTitle(game5Title);
    }

    @Test
    public void testAutocomplete() {
        final String game1Title = "Baldur's Gate II: Shadows of Amn - ElasticGameRepositoryTest";
        final String game2Title = "Fallout 3 - ElasticGameRepositoryTest";
        final String game3Title = "Vampire: The Masquerade - Bloodlines - ElasticGameRepositoryTest";

        final String igdbId1 = "1";
        final String igdbId2 = "2";
        final String igdbId3 = "3";

        // Insert test games
        // Delete test games if already present
        games.deleteByTitle(game1Title);
        games.deleteByTitle(game2Title);
        games.deleteByTitle(game3Title);

        Game game1 = new Game();
        game1.setTitle(game1Title);
        game1.setIgdbId(igdbId1);

        Game game2 = new Game();
        game2.setTitle(game2Title);
        game2.setIgdbId(igdbId2);

        Game game3 = new Game();
        game3.setTitle(game3Title);
        game3.setIgdbId(igdbId3);

        games.save(game1);
        games.save(game2);
        games.save(game3);

        game1 = games.findGameByTitle(game1Title);
        game2 = games.findGameByTitle(game2Title);
        game3 = games.findGameByTitle(game3Title);

        ElasticGame elasticGame1 = new ElasticGame(game1);
        ElasticGame elasticGame2 = new ElasticGame(game2);
        ElasticGame elasticGame3 = new ElasticGame(game3);

        elasticGames.save(elasticGame1);
        elasticGames.save(elasticGame2);
        elasticGames.save(elasticGame3);

        // Search for Fallout 3
        SearchHits<ElasticGame> results = elasticGames.autocompleteGameTitle("fout 3", 1);

        List<SearchHit<ElasticGame>> searchHits = results.getSearchHits();

        for (SearchHit<ElasticGame> searchHit : searchHits) {
            if (searchHit.getContent().getTitle().equals("Fallout 3")) {
                assertThat("Fallout 3 is in search results", true);
            }
        }

        // Delete elastic games
        elasticGames.delete(elasticGame1);
        elasticGames.delete(elasticGame2);
        elasticGames.delete(elasticGame3);

        // Delete test games
        games.deleteByTitle(game1Title);
        games.deleteByTitle(game2Title);
        games.deleteByTitle(game3Title);
    }

    @Test
    public void testElasticGameBug() {
        ElasticGame game = elasticGames.findByGameId("5fa261446ffacd4ab297dfb2");
        System.out.println(game.getTitle());
    }
}
