package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Qualifier("elasticsearchOperations")
    //private RestHighLevelClient elasticSearch;
    @Autowired
    private ElasticsearchOperations elasticSearch;

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
        //elasticGames.deleteByGameId(game1Id);
        //elasticGames.deleteByGameId(game2Id);
        //elasticGames.deleteByGameId(game3Id);
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
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", "Faou 3")
                        .operator(Operator.AND)
                        .fuzziness(Fuzziness.TWO)
                        .prefixLength(1)
                        .fuzzyTranspositions(true))
                .build();
        SearchHits<ElasticGame> gameResults = elasticSearch.search(searchQuery, ElasticGame.class, IndexCoordinates.of("games"));

        assertThat(gameResults.getTotalHits(), is(1L));
        System.out.println(gameResults.getSearchHit(0).getContent().getTitle());
        assertThat(gameResults.getSearchHit(0).getContent().getTitle(), is("Fallout 3"));

        // Delete elastic games
        elasticGames.deleteByGameId(game1Id);
        elasticGames.deleteByGameId(game2Id);
        elasticGames.deleteByGameId(game3Id);
    }
}
