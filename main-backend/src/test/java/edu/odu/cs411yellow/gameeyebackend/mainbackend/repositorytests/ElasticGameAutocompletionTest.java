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
        final String game1Id = "5f78f69e7588682adc444b04";
        final String game2Id = "5f78f69e7588682adc444b08";
        final String game3Id = "5f78f69d7588682adc444aff";
        // Delete elastic games if already present
        elasticGames.deleteByGameId(game1Id);
        elasticGames.deleteByGameId(game2Id);
        elasticGames.deleteByGameId(game3Id);

        Game game1 = games.findGameById(game1Id);
        Game game2 = games.findGameById(game2Id);
        Game game3 = games.findGameById(game3Id);

        ElasticGame elasticGame1 = new ElasticGame(game1);
        ElasticGame elasticGame2 = new ElasticGame(game2);
        ElasticGame elasticGame3 = new ElasticGame(game3);

        elasticGames.save(elasticGame1);
        elasticGames.save(elasticGame2);
        elasticGames.save(elasticGame3);

        elasticGame1 = elasticGames.findByGameId(game1Id);
        elasticGame2 = elasticGames.findByGameId(game2Id);
        elasticGame3 = elasticGames.findByGameId(game3Id);

        assertThat(elasticGame1.getGameId(), is(game1Id));
        assertThat(elasticGame1.getTitle(), is(game1.getTitle()));

        assertThat(elasticGame2.getGameId(), is(game2Id));
        assertThat(elasticGame2.getTitle(), is(game2.getTitle()));

        assertThat(elasticGame2.getGameId(), is(game2Id));
        assertThat(elasticGame2.getTitle(), is(game2.getTitle()));

        // Delete elastic games
        elasticGames.deleteByGameId(game1Id);
        elasticGames.deleteByGameId(game2Id);
        elasticGames.deleteByGameId(game3Id);
    }

    @Test
    public void testAutocomplete() {
        final String game1Id = "5f78f69e7588682adc444b04";
        final String game2Id = "5f78f69e7588682adc444b08";
        final String game3Id = "5f78f69d7588682adc444aff";

        // Delete elastic games if already present
        elasticGames.deleteByGameId(game1Id);
        elasticGames.deleteByGameId(game2Id);
        elasticGames.deleteByGameId(game3Id);

        Game game1 = games.findGameById(game1Id);
        Game game2 = games.findGameById(game2Id);
        Game game3 = games.findGameById(game3Id);

        ElasticGame elasticGame1 = new ElasticGame(game1);
        ElasticGame elasticGame2 = new ElasticGame(game2);
        ElasticGame elasticGame3 = new ElasticGame(game3);

        elasticGames.save(elasticGame1);
        elasticGames.save(elasticGame2);
        elasticGames.save(elasticGame3);

        // Search for Fallout 3
        SearchHits<ElasticGame> results = autocompletionService.autocompleteGameTitle("fout 3", 5);

        assertThat(results.getTotalHits(), is(1L));
        assertThat(results.getSearchHit(0).getContent().getTitle(), is("Fallout 3"));

        // Delete elastic games
        elasticGames.deleteByGameId(game1Id);
        elasticGames.deleteByGameId(game2Id);
        elasticGames.deleteByGameId(game3Id);
    }
}
