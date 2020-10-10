package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.AutocompletionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class ElasticGameAutocompletionTest {
    @Autowired
    private ElasticGameRepository elasticGames;

    @Autowired
    private GameRepository games;

    @Autowired
    AutocompletionService autocompletionService;

    @Test
    public void testInsertElasticGames() {
        final String game1Title = "Baldur's Gate II: Shadows of Amn";
        final String game2Title = "Vampire: The Masquerade - Bloodlines";
        final String game3Title = "Fallout 3";
        final String game4Title = "Fallout 2";
        final String game5Title = "Fallout: New Vegas";

        // Delete elastic games if already present
        elasticGames.deleteByTitle(game1Title);
        elasticGames.deleteByTitle(game2Title);
        elasticGames.deleteByTitle(game3Title);
        elasticGames.deleteByTitle(game4Title);
        elasticGames.deleteByTitle(game5Title);

        Game game1 = games.findGameByTitle(game1Title);
        Game game2 = games.findGameByTitle(game2Title);
        Game game3 = games.findGameByTitle(game3Title);
        Game game4 = games.findGameByTitle(game4Title);
        Game game5 = games.findGameByTitle(game5Title);

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

        elasticGame1 = elasticGames.findByTitle(game1Title);
        elasticGame2 = elasticGames.findByTitle(game2Title);
        elasticGame3 = elasticGames.findByTitle(game3Title);
        elasticGame4 = elasticGames.findByTitle(game4Title);
        elasticGame5 = elasticGames.findByTitle(game5Title);

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
        elasticGames.deleteByTitle(game1Title);
        elasticGames.deleteByTitle(game2Title);
        elasticGames.deleteByTitle(game3Title);
        elasticGames.deleteByTitle(game4Title);
        elasticGames.deleteByTitle(game5Title);
    }

    @Test
    public void testAutocomplete() {
        final String game1Title = "Baldur's Gate II: Shadows of Amn";
        final String game2Title = "Fallout 3";
        final String game3Title = "Vampire: The Masquerade - Bloodlines";

        // Delete elastic games if already present
        elasticGames.deleteByTitle(game1Title);
        elasticGames.deleteByTitle(game1Title);
        elasticGames.deleteByTitle(game1Title);

        Game game1 = games.findGameByTitle(game1Title);
        Game game2 = games.findGameByTitle(game2Title);
        Game game3 = games.findGameByTitle(game3Title);

        ElasticGame elasticGame1 = new ElasticGame(game1);
        ElasticGame elasticGame2 = new ElasticGame(game2);
        ElasticGame elasticGame3 = new ElasticGame(game3);

        elasticGames.save(elasticGame1);
        elasticGames.save(elasticGame2);
        elasticGames.save(elasticGame3);

        // Search for Fallout 3
        SearchHits<ElasticGame> results = autocompletionService.autocompleteGameTitle("fout 3", 1);

        assertThat(results.getTotalHits(), is(1L));
        assertThat(results.getSearchHit(0).getContent().getTitle(), is("Fallout 3"));

        // Delete elastic games
        elasticGames.deleteByTitle(game1Title);
        elasticGames.deleteByTitle(game2Title);
        elasticGames.deleteByTitle(game3Title);
    }
}
