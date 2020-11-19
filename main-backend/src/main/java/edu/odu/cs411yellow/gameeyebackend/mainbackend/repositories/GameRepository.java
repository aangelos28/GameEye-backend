package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GameRepository extends MongoRepository<Game, String>, GameRepositoryCustom {
    Game findByTitle(String title);

    Game findGameByTitle(String title);

    Game findGameById(String id);

    List<Game> findGamesById(Collection<String> ids);

    Game findByIgdbId(String idgbIdb);

    boolean existsByIgdbId(String igdbId);

    boolean existsByTitle(String title);

    @Override
    boolean existsById(String id);

    @Override
    void deleteById(String id);

    void deleteByIgdbId(String id);

    void deleteByTitle(String title);
}
