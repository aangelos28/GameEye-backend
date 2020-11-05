package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticGameRepository extends ElasticsearchRepository<ElasticGame, String>, ElasticGameRepositoryCustom {
    ElasticGame findByTitle(String title);

    boolean existsByTitle(String title);

    ElasticGame findByGameId(String id);

    void deleteById(String id);

    void deleteByTitle(String title);
}
