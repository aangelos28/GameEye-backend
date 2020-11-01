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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IgdbReplicationService {
    IgdbService igdbService;
    GameRepository gameRepository;
    ElasticGameRepository elasticRepository;

    Logger logger = LoggerFactory.getLogger(IgdbReplicationService.class);

    @Autowired
    public IgdbReplicationService(IgdbService igdbService, GameRepository gameRepository,
                                  ElasticGameRepository elasticRepository) {
        this.igdbService = igdbService;
        this.gameRepository = gameRepository;
        this.elasticRepository = elasticRepository;
    }

    public String replicateIgdbByRange(int minId, int maxId, int limit) {
        logger.info(String.format("Replicating potentially %1$s games; range: %2$s-%3$s; limit: %4$s.",
                                  (maxId - minId + 1), minId, maxId, limit));
        List<Game> newGames = igdbService.retrieveGamesByRangeWithLimit(minId, maxId, limit);

        int bufferSize = 500;

        List<Game> mongoGameBuffer = new ArrayList<>(bufferSize);
        List<ElasticGame> elasticGameBuffer = new ArrayList<>(bufferSize);

        int updatedGameCount = 0;
        int newGameCount = 0;

        for (Game newGame: newGames) {
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

                    // Add updated existing game to mongoGameBuffer
                    existingGame.setLastUpdated(new Date());
                    mongoGameBuffer.add(existingGame);

                    updatedGameCount++;
                }
                else {
                    // Add new game to mongoGameBuffer
                    newGame.setLastUpdated(new Date());
                    mongoGameBuffer.add(newGame);

                    // Add new game to Elasticsearch array if it does not exist
                    String title = newGame.getTitle();
                    if(!elasticRepository.existsByTitle(title)) {
                        ElasticGame elasticGame = new ElasticGame(newGame);
                        elasticGameBuffer.add(elasticGame);
                    }

                    newGameCount++;
                }
            }

            // Bulk insert every bufferSize documents to mongo
            if (mongoGameBuffer.size() % bufferSize == 0) {
                gameRepository.saveAll(mongoGameBuffer);
            }

            // Bulk insert every bufferSize documents to elastic
            if (elasticGameBuffer.size() % bufferSize == 0) {
                elasticRepository.saveAll(elasticGameBuffer);
            }
        }

        // Save all remaining games to mongo and elastic
        gameRepository.saveAll(mongoGameBuffer);
        mongoGameBuffer.clear();
        elasticRepository.saveAll(elasticGameBuffer);
        elasticGameBuffer.clear();

        String status = "Finished replication. " + "Added " + newGameCount + " new games and " + "updated " + updatedGameCount + ".";

        logger.info(status);

        return status;
    }
}
