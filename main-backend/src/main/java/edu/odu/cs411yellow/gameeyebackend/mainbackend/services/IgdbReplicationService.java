package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.bson.types.ObjectId;
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

    public String replicateGamesByRange(int minId, int maxId, int limit) {
        logger.info(String.format("Replicating potentially %1$s games; range: %2$s-%3$s; limit: %4$s.",
                                  (maxId - minId + 1), minId, maxId, limit));

        int remainder = maxId - minId + 1;
        int currentMaxId = maxId;
        int currentMinId = minId;
        int igdbGameBufferSize = 500;
        int mongoBufferSize = 500;
        int elasticBufferSize = 500;

        // Stores igdb games, which may be new or existing in mongo and elastic
        List<Game> igdbGameBuffer = new ArrayList<>(igdbGameBufferSize);
        List<Game> mongoGameBuffer = new ArrayList<>(mongoBufferSize);
        List<ElasticGame> elasticGameBuffer = new ArrayList<>(elasticBufferSize);

        long numberOfRequestsWithLimit = 1;
        int updatedGameCount = 0;
        int newGameCount = 0;
        while (remainder > 0) {
            if (remainder > limit) {
                currentMaxId = currentMinId + limit;
                remainder -= limit;

                igdbGameBuffer.addAll(igdbService.retrieveGamesByRangeWithLimit(currentMinId, currentMaxId, limit));

                logger.info("Retrieved games in ID range " + currentMinId + "-" + currentMaxId + ".");

                currentMinId = currentMaxId + 1;
            } else {
                currentMaxId = maxId;
                igdbGameBuffer.addAll(igdbService.retrieveGamesByRangeWithLimit(currentMinId, currentMaxId, limit));

                logger.info("Retrieved games in ID range " + currentMinId + "-" + currentMaxId + ".");

                remainder = (remainder - currentMaxId - currentMinId + 1);
            }

            // Bulk insert new and updated games if greater than or equal to the igdbGameBufferSize
            if ((limit * numberOfRequestsWithLimit) >= igdbGameBufferSize) {
                for (Game igdbGame: igdbGameBuffer) {
                    // Check that the IGDB game is not null
                    if (!igdbGame.getIgdbId().equals("")) {
                        // Update an existing IGDB game in games collection
                        if (gameRepository.existsByIgdbId(igdbGame.getIgdbId())) {
                            Game updatedGame = updateExistingGame(igdbGame);
                            mongoGameBuffer.add(updatedGame);

                            updatedGameCount++;
                        }
                        else {
                            // Add new game to mongoGameBuffer
                            igdbGame.setId(ObjectId.get().toString());
                            mongoGameBuffer.add(igdbGame);

                            // Add new game to elasticGameBuffer if it does not exist
                            if (!(elasticRepository.existsByTitle(igdbGame.getTitle()))) {
                                ElasticGame elasticGame = new ElasticGame(igdbGame);
                                elasticGameBuffer.add(elasticGame);
                            }

                            newGameCount++;
                        }
                    }
                }

                igdbGameBuffer.clear();
            }



            numberOfRequestsWithLimit++;
        }



        // Save all remaining games to mongo and elastic
        gameRepository.saveAll(mongoGameBuffer);
        mongoGameBuffer.clear();
        elasticRepository.saveAll(elasticGameBuffer);
        elasticGameBuffer.clear();

        String status = String.format("Finished replication. Added %1$s new games and updated %2$s existing games.",
                                       newGameCount, updatedGameCount);

        logger.info(status);

        return status;
    }

    private Game updateExistingGame(Game igdbGame) {
        Game existingGame = gameRepository.findByIgdbId(igdbGame.getIgdbId());

        // Update logoUrls, platforms, and genres
        existingGame.setLogoUrl(igdbGame.getLogoUrl());
        existingGame.setPlatforms(igdbGame.getPlatforms());
        existingGame.setGenres(igdbGame.getGenres());

        // Update sourceUrls
        SourceUrls newSourceUrls = igdbGame.getSourceUrls();
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

        return existingGame;
    }

    // Add updated existing game to mongoGameBuffer
        existingGame.setLastUpdated(new Date());
        mongoGameBuffer.add(existingGame);
}
