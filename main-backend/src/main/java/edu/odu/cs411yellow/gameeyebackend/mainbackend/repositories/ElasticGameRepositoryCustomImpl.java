package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticGameRepositoryCustomImpl implements ElasticGameRepositoryCustom {

    private final ElasticsearchOperations elasticSearch;

    @Autowired
    ElasticGameRepositoryCustomImpl(@Qualifier("elasticsearchOperations") ElasticsearchOperations elasticSearch) {
        this.elasticSearch = elasticSearch;
    }

    /**
     * Gets autocompletion suggestions for a given game title.
     *
     * @param title Tile of the game, partial or full
     * @param maxResults Maximum autocompletion results to get
     * @return List of autocompleted games
     */
    @Override
    public SearchHits<ElasticGame> autocompleteGameTitle(String title, int maxResults) {
        // Query ElasticSearch
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", title)
                        .fuzziness(Fuzziness.TWO)
                        .prefixLength(1))
                .build();
        searchQuery.setMaxResults(maxResults);

        return elasticSearch.search(searchQuery, ElasticGame.class, IndexCoordinates.of("games"));
    }
}
