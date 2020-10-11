package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public void replicateIgdbByRange(int minId, int maxId, int limit) throws JsonProcessingException {
        logger.info(String.format("Replicating potentially %1$s games; range: %2$s-%3$s; limit: %4$s.",
                                  (maxId - minId + 1), minId, maxId, limit));
        List<Game> games = igdbService.getGamesByRange(minId, maxId, limit);

        int updatedGames = 0;
        int newGames = 0;

        for (Game newGame: games) {
            // Check that the new game is not null
            if (!newGame.getIgdbId().equals("")) {
                // Update if new game exists by igdbId
                if (gameRepository.existsByIgdbId(newGame.getIgdbId())) {
                    Game existingGame = gameRepository.findByIgdbId(newGame.getIgdbId());

                    // Update platforms and genres
                    existingGame.setPlatforms(newGame.getPlatforms());
                    existingGame.setGenres(newGame.getGenres());

                    // Update sourceUrls
                    SourceUrls newSourceUrls = newGame.getSourceUrls();
                    SourceUrls existingSourceUrls = existingGame.getSourceUrls();

                    if (!newSourceUrls.getPublisherUrl().equals("")) {
                        existingSourceUrls.setPublisherUrl(newSourceUrls.getPublisherUrl());
                    }
                    if (!newSourceUrls.getSteamUrl().equals("")) {
                        existingSourceUrls.setSteamUrl(newSourceUrls.getSteamUrl());
                    }
                    if (!newSourceUrls.getSubRedditUrl().equals("")) {
                        existingSourceUrls.setSubRedditUrl(newSourceUrls.getSubRedditUrl());
                    }
                    if (!newSourceUrls.getTwitterUrl().equals("")) {
                        existingSourceUrls.setTwitterUrl(newSourceUrls.getTwitterUrl());
                    }

                    // Save updated existing game
                    existingGame.setLastUpdated(new Date());
                    gameRepository.save(existingGame);
                    updatedGames++;
                }
                else {
                    // Save new game
                    newGame.setLastUpdated(new Date());
                    gameRepository.save(newGame);
                    newGames++;
                }
            }
        }
        logger.info("Finished replication. " + "Added " + newGames + " new games and " +
                    "updated " + updatedGames + ".");

    }
}
