package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import org.springframework.stereotype.Repository;

/**
 * Custom methods for game repository.
 */
@Repository
public interface GameRepositoryCustom {
    void incrementWatchers(String gameId);
    void decrementWatchers(String gameId);
}
