package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
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
    ElasticGameRepository elasticGames;

    Logger logger = LoggerFactory.getLogger(IgdbReplicatorService.class);

    @Autowired
    public IgdbReplicatorService(IgdbService igdbService, GameRepository gameRepository,
                                 ElasticGameRepository elasticGames) {
        this.igdbService = igdbService;
        this.gameRepository = gameRepository;
        this.elasticGames = elasticGames;
    }

    public void replicateIgdbByRange(int minId, int maxId, int limit) {
        logger.info(String.format("Replicating potentially %1$s games; range: %2$s-%3$s; limit: %4$s.",
                                  (maxId - minId + 1), minId, maxId, limit));
        List<Game> games = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);

        int updatedGames = 0;
        int newGames = 0;

        for (Game newGame: games) {
            // Check that the new game is not null
            if (!newGame.getIgdbId().equals("")) {
                // Update if new game exists by igdbId
                if (gameRepository.existsByIgdbId(newGame.getIgdbId())) {
                    Game existingGame = gameRepository.findByIgdbId(newGame.getIgdbId());

                    // Update logoUrls, platforms, and genres
                    existingGame.setLogoUrl(newGame.getLogoUrl());
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

                    // Save new urls in existing
                    existingGame.setSourceUrls(existingSourceUrls);

                    // Save updated existing game
                    existingGame.setLastUpdated(new Date());
                    gameRepository.save(existingGame);

                    // Update Elasticsearch database
                    ElasticGame elasticGame = new ElasticGame(existingGame);
                    elasticGames.save(elasticGame);

                    updatedGames++;
                }
                else {
                    // Save new game
                    newGame.setLastUpdated(new Date());
                    gameRepository.save(newGame);

                    // Add new game to Elasticsearch database
                    ElasticGame elasticGame = new ElasticGame(newGame);
                    elasticGames.save(elasticGame);

                    newGames++;
                }
            }
        }
        logger.info("Finished replication. " + "Added " + newGames + " new games and " +
                    "updated " + updatedGames + ".");

    }
}
