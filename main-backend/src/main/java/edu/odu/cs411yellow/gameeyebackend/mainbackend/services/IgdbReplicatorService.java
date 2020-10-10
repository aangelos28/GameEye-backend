package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IgdbReplicatorService {
    IgdbService igdbService;
    GameRepository gameRepository;

    Logger logger = LoggerFactory.getLogger(IgdbReplicatorService.class);

    @Autowired
    public IgdbReplicatorService(IgdbService igdbService, GameRepository gameRepository) {
        this.igdbService = igdbService;
        this.gameRepository = gameRepository;
    }

    public void replicateIgdbByRange(int minId, int maxId) throws JsonProcessingException {
        logger.info("Replicating " + (maxId - minId + 1) + " games; range: " + minId + "-" + maxId);
        List<Game> games = igdbService.getGamesByRange(minId, maxId);

        int updatedGames = 0;
        int newGames = 0;

        for (Game newGame: games) {
            // Check that the new game is not null
            if (newGame.getIgdbId() != "") {
                // Update if new game exists by igdbId
                if (gameRepository.existsByIgdbId(newGame.getIgdbId())) {
                    Game existingGame = gameRepository.findByIgdbId(newGame.getIgdbId());

                    existingGame.setTitle(newGame.getTitle());
                    existingGame.setPlatforms(newGame.getPlatforms());
                    existingGame.setGenres(newGame.getGenres());
                    existingGame.setSourceUrls(newGame.getSourceUrls());

                    // Save updated existing game
                    gameRepository.save(existingGame);
                    updatedGames++;
                }
                else {
                    // Save new game
                    gameRepository.save(newGame);
                    newGames++;
                }
            }
        }
        logger.info("Finished replication. " + "Added " + newGames + " new games and " +
                    "updated " + updatedGames + ".");

    }
}
