package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    Game findByTitle(String title);

    Game findGameById(String id);

    Game findByIgdbId(String igdbId);

    void deleteById(String id);

    boolean existsById(Game game);

    boolean existsByIgdbId(String igdbId);

    void deleteByIgdbId(String igdbId);
}
