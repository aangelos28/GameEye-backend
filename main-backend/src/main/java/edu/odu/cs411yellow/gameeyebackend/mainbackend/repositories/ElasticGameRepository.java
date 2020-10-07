package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticGameRepository extends ElasticsearchRepository<ElasticGame, String> {
    ElasticGame findByTitle(String title);

    ElasticGame findByGameId(String gameId);

    void deleteById(String id);

    void deleteByGameId(String gameId);
}
