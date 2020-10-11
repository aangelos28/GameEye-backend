package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Game, String>, GameRepositoryCustom {
    Game findByTitle(String title);

    Game findGameById(String id);

    Game findGameByTitle(String title);

    void deleteById(String id);

    void deleteByTitle(String title);

    boolean existsById(Game game);
}
