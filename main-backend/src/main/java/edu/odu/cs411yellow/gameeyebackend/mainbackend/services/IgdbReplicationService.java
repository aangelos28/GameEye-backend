package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IgdbReplicationService {
    IgdbService igdbService;
    GameService gameService;
    ElasticGameService elasticService;
    GameRepository gameRepository;

    Logger logger = LoggerFactory.getLogger(IgdbReplicationService.class);

    @Autowired
    public IgdbReplicationService(IgdbService igdbService, GameService gameService,
                                  ElasticGameService elasticService, GameRepository gameRepository) {
        this.igdbService = igdbService;
        this.gameService = gameService;
        this.elasticService = elasticService;
        this.gameRepository = gameRepository;
    }

    public String replicateGamesByIdRange(int minId, int maxId, int limit) {
        logger.info(String.format("Attempting to replicate %1$s games. IGDB ID range: %2$s-%3$s. Result limit per API request: %4$s.",
                                  (maxId - minId + 1), minId, maxId, limit));

        int remainder = maxId - minId + 1;
        int currentMaxId;
        int currentMinId = minId;
        int igdbGameBufferSize = 500;
        int mongoBufferSize = 1000;
        int elasticBufferSize = 10000;

        // Stores igdb games, which may be new or existing in mongo and elastic
        List<Game> igdbGameBuffer = new ArrayList<>(igdbGameBufferSize);
        List<Game> mongoGameBuffer = new ArrayList<>(mongoBufferSize);
        List<ElasticGame> elasticGameBuffer = new ArrayList<>(elasticBufferSize);

        long numberOfRequestsWithLimit = 1;
        int updatedGameCount = 0;
        int newGameCount = 0;
        int newElasticGameCount = 0;
        int currentElasticBufferCount = 0;
        int currentMongoBufferCount = 0;
        while (remainder > 0) {
            if (remainder > limit) {
                currentMaxId = currentMinId + limit;
                remainder -= limit;

                igdbGameBuffer.addAll(igdbService.retrieveGamesByRangeWithLimit(currentMinId, currentMaxId, limit));

                logger.info(String.format("Retrieved games in ID range %1$s-%2$s", currentMinId, currentMaxId));

                currentMinId = currentMaxId + 1;
            } else {
                currentMaxId = maxId;
                igdbGameBuffer.addAll(igdbService.retrieveGamesByRangeWithLimit(currentMinId, currentMaxId, limit));

                logger.info(String.format("Retrieved games in ID range %1$s-%2$s", currentMinId, currentMaxId));

                remainder = (remainder - currentMaxId - currentMinId + 1);
            }

            // Fill mongo/elastic buffers with new and updated games if greater than or equal to the igdbGameBufferSize
            // Fill mongo/elastic buffers if remainder is negative, as there are no more games to retrieve.
            if ((limit * numberOfRequestsWithLimit) >= igdbGameBufferSize || remainder <= 0) {
                for (Game igdbGame: igdbGameBuffer) {
                    // Check that the IGDB game is not null
                    if (!igdbGame.getIgdbId().equals("")) {
                        // Update an existing IGDB game in games collection
                        if (gameService.existsByIgdbId(igdbGame.getIgdbId())) {
                            // Update game and elastic search if necessary
                            updateExistingGame(igdbGame);

                            updatedGameCount++;
                        }
                        else {
                            // Add new game to mongoGameBuffer
                            igdbGame.setId(ObjectId.get().toString());
                            mongoGameBuffer.add(igdbGame);

                            // Add new game to elasticGameBuffer
                            ElasticGame elasticGame = new ElasticGame(igdbGame);
                            elasticGameBuffer.add(elasticGame);

                            newElasticGameCount++;
                            currentElasticBufferCount++;
                            currentMongoBufferCount++;
                            newGameCount++;
                        }
                    }

                    // Save games to mongo if buffer is >= to buffer size
                    if (currentMongoBufferCount >= mongoBufferSize) {
                        gameService.saveAll(mongoGameBuffer);
                        mongoGameBuffer.clear();

                        currentMongoBufferCount = 0;
                    }

                    // Save games to elastic search buffer is >= to buffer size
                    if (currentElasticBufferCount >= elasticBufferSize) {
                        elasticService.saveAll(elasticGameBuffer);
                        elasticGameBuffer.clear();

                        currentElasticBufferCount = 0;
                    }
                }

                igdbGameBuffer.clear();
            }

            numberOfRequestsWithLimit++;
        }

        // Save all remaining games to mongo and elastic
        gameService.saveAll(mongoGameBuffer);
        mongoGameBuffer.clear();

        elasticService.saveAll(elasticGameBuffer);
        elasticGameBuffer.clear();

        String status = String.format("Finished replication. MongoDB status: Added %1$s new games and updated %2$s existing games. Elasticsearch status: Added %3$s new game titles.",
                                       newGameCount, updatedGameCount, newElasticGameCount);

        logger.info(status);

        return status;
    }

    public String replicateGamesByTitles(final List<String> gameTitles, int limit) {
        logger.info(String.format("Attempting to replicate %1$s games. Result limit per API request: %2$s.",
                                  gameTitles.size(), limit));

        List<String> titles = new ArrayList<>(gameTitles);

        int remainder = titles.size();
        int igdbGameBufferSize = 500;
        int mongoBufferSize = 1000;
        int elasticBufferSize = 10000;

        // Stores igdb games, which may be new or existing in mongo and elastic
        List<Game> igdbGameBuffer = new ArrayList<>(igdbGameBufferSize);
        List<Game> mongoGameBuffer = new ArrayList<>(mongoBufferSize);
        List<ElasticGame> elasticGameBuffer = new ArrayList<>(elasticBufferSize);

        long numberOfRequestsWithLimit = 1;
        int updatedGameCount = 0;
        int newGameCount = 0;
        int newElasticGameCount = 0;
        int currentElasticBufferCount = 0;
        int currentMongoBufferCount = 0;
        while (remainder > 0) {
            if (remainder > limit) {
                remainder -= limit;

                igdbGameBuffer.addAll(igdbService.retrieveGamesByTitles(titles.subList(0, limit - 1), limit));
                titles.subList(0, limit - 1).clear();

                logger.info(String.format("Retrieved %1$s games. %2$s games remaining.", limit, remainder));
            } else {
                igdbGameBuffer.addAll(igdbService.retrieveGamesByTitles(titles, limit));
                logger.info(String.format("Retrieved %1$s games.", titles.size()));
                titles.clear();

                remainder = titles.size();
                logger.info(String.format("%1$s games remaining.", remainder));
            }

            // Fill mongo/elastic buffers with new and updated games if greater than or equal to the igdbGameBufferSize
            // Fill mongo/elastic buffers if remainder is negative, as there are no more games to retrieve.
            if ((limit * numberOfRequestsWithLimit) >= igdbGameBufferSize || remainder <= 0) {
                for (Game igdbGame: igdbGameBuffer) {
                    // Check that the IGDB game is not null
                    if (!igdbGame.getIgdbId().equals("")) {
                        // Update an existing IGDB game in games collection
                        if (gameService.existsByIgdbId(igdbGame.getIgdbId())) {
                            // Update game and elastic search if necessary
                            updateExistingGame(igdbGame);

                            updatedGameCount++;
                        }
                        else {
                            // Add new game to mongoGameBuffer
                            igdbGame.setId(ObjectId.get().toString());
                            mongoGameBuffer.add(igdbGame);

                            // Add new game to elasticGameBuffer
                            ElasticGame elasticGame = new ElasticGame(igdbGame);
                            elasticGameBuffer.add(elasticGame);

                            newElasticGameCount++;
                            currentElasticBufferCount++;
                            currentMongoBufferCount++;
                            newGameCount++;
                        }
                    }

                    // Save games to mongo if buffer is >= to buffer size
                    if (currentMongoBufferCount >= mongoBufferSize) {
                        gameService.saveAll(mongoGameBuffer);
                        mongoGameBuffer.clear();

                        currentMongoBufferCount = 0;
                    }

                    // Save games to elastic search buffer is >= to buffer size
                    if (currentElasticBufferCount >= elasticBufferSize) {
                        elasticService.saveAll(elasticGameBuffer);
                        elasticGameBuffer.clear();

                        currentElasticBufferCount = 0;
                    }
                }

                igdbGameBuffer.clear();
            }

            numberOfRequestsWithLimit++;
        }

        // Save all remaining games to mongo and elastic
        gameService.saveAll(mongoGameBuffer);
        mongoGameBuffer.clear();

        elasticService.saveAll(elasticGameBuffer);
        elasticGameBuffer.clear();

        String status = String.format("Finished replication. MongoDB status: Added %1$s new games and updated %2$s existing games. Elasticsearch status: Added %3$s new game titles.",
                newGameCount, updatedGameCount, newElasticGameCount);

        logger.info(status);

        return status;
    }

    public String replicateNewReleases(final int num, int limit) {
        logger.info(String.format("Attempting to replicate the most recent %1$s games. Result limit per API request: %2$s.",
                    num, limit));

        int remainder = num;
        int igdbGameBufferSize = 500;
        int mongoBufferSize = 1000;
        int elasticBufferSize = 10000;

        // Stores igdb games, which may be new or existing in mongo and elastic
        List<Game> igdbGameBuffer = new ArrayList<>(igdbGameBufferSize);
        List<Game> mongoGameBuffer = new ArrayList<>(mongoBufferSize);
        List<ElasticGame> elasticGameBuffer = new ArrayList<>(elasticBufferSize);

        long numberOfRequestsWithLimit = 1;
        int updatedGameCount = 0;
        int newGameCount = 0;
        int newElasticGameCount = 0;
        int currentElasticBufferCount = 0;
        int currentMongoBufferCount = 0;
        long currentOldestReleaseDate = 0;
        while (remainder > 0) {
            if (remainder > limit) {
                remainder -= limit;

                List<Game> games = igdbService.retrieveNewReleases(currentOldestReleaseDate, limit);

                for (Game game: games) {
                    if (igdbGameBuffer.indexOf(game) != -1) {
                        igdbGameBuffer.add(game);
                    }
                }

                logger.info(String.format("Retrieved %1$s games. %2$s games remaining.", games.size(), remainder));
            } else {
                List<Game> games = igdbService.retrieveNewReleases(currentOldestReleaseDate, remainder);

                for (Game game: games) {
                    if (igdbGameBuffer.indexOf(game) != -1) {
                        igdbGameBuffer.add(game);
                    }
                }

                logger.info(String.format("Retrieved %1$s games.", games.size()));

                remainder -= games.size();
                logger.info(String.format("%1$s games remaining.", remainder));
            }

            // Fill mongo/elastic buffers with new and updated games if greater than or equal to the igdbGameBufferSize
            // Fill mongo/elastic buffers if remainder is negative, as there are no more games to retrieve.
            if ((limit * numberOfRequestsWithLimit) >= igdbGameBufferSize || remainder <= 0) {
                for (Game igdbGame: igdbGameBuffer) {
                    // Check that the IGDB game is not null
                    if (!igdbGame.getIgdbId().equals("")) {
                        long releaseDate = igdbGame.getReleaseDate().getTime() / 1000;
                        if (releaseDate < currentOldestReleaseDate) {
                            currentOldestReleaseDate = releaseDate;
                        }

                        // Update an existing IGDB game in games collection
                        if (gameService.existsByIgdbId(igdbGame.getIgdbId())) {
                            // Update game and elastic search if necessary
                            updateExistingGame(igdbGame);

                            updatedGameCount++;
                        }
                        else {
                            // Add new game to mongoGameBuffer
                            igdbGame.setId(ObjectId.get().toString());
                            mongoGameBuffer.add(igdbGame);

                            // Add new game to elasticGameBuffer
                            ElasticGame elasticGame = new ElasticGame(igdbGame);
                            elasticGameBuffer.add(elasticGame);

                            newElasticGameCount++;
                            currentElasticBufferCount++;
                            currentMongoBufferCount++;
                            newGameCount++;
                        }
                    }

                    // Save games to mongo if buffer is >= to buffer size
                    if (currentMongoBufferCount >= mongoBufferSize) {
                        gameService.saveAll(mongoGameBuffer);
                        mongoGameBuffer.clear();

                        currentMongoBufferCount = 0;
                    }

                    // Save games to elastic search buffer is >= to buffer size
                    if (currentElasticBufferCount >= elasticBufferSize) {
                        elasticService.saveAll(elasticGameBuffer);
                        elasticGameBuffer.clear();

                        currentElasticBufferCount = 0;
                    }
                }

                igdbGameBuffer.clear();
            }

            numberOfRequestsWithLimit++;
        }

        // Save all remaining games to mongo and elastic
        gameService.saveAll(mongoGameBuffer);
        mongoGameBuffer.clear();

        elasticService.saveAll(elasticGameBuffer);
        elasticGameBuffer.clear();

        String status = String.format("Finished replication. MongoDB status: Added %1$s new games and updated %2$s existing games. Elasticsearch status: Added %3$s new game titles.",
                newGameCount, updatedGameCount, newElasticGameCount);

        logger.info(status);

        return status;
    }

    private void updateExistingGame(Game igdbGame) {
        String gameId = gameRepository.findGameIdByIgdbId(igdbGame.getIgdbId());

        // Update existing game title with new IGDB title
        gameRepository.updateGameTitle(gameId, igdbGame.getTitle());

        // Update logoUrl, platforms, release date, genres, and sourceUrls
        gameRepository.updateLogoPlatformsReleaseDateGenresSourceUrls(gameId,
                igdbGame.getLogoUrl(),
                igdbGame.getPlatforms(),
                igdbGame.getGenres(),
                igdbGame.getReleaseDate(),
                igdbGame.getSourceUrls());

        // Update elastic game title, logoUrl, and releaseDate
        ElasticGame elasticGame = new ElasticGame(igdbGame);
        elasticGame.setGameId(gameId);

        elasticService.updateTitleLogoReleaseDate(elasticGame);
    }

    public int bulkUpdateElasticSearch(List<Game> games) {
        List<ElasticGame> elasticGames = new ArrayList<>();

        for (Game game: games) {
            String gameId = gameRepository.findGameIdByIgdbId(game.getIgdbId());
            ElasticGame elasticGame = new ElasticGame(game);
            elasticGame.setGameId(gameId);

            elasticGames.add(elasticGame);
        }

        return elasticService.bulkUpdateElastic(elasticGames);
    }

    public String updateGamesCollection(int limit) {
        List<String> ids = gameRepository.findAllIgdbIds();

        logger.info(String.format("Attempting to updates the %1$s games in the games collection. Result limit per API request: %2$s.",
                    ids.size(), limit));

        int remainder = ids.size();
        int igdbGameBufferSize = 500;

        // Stores igdb games
        List<Game> igdbGameBuffer = new ArrayList<>(igdbGameBufferSize);

        int numberOfRequestsWithLimit = 1;
        int updatedCount = 0;
        while (remainder > 0) {
            if (remainder > limit) {
                remainder -= limit;

                igdbGameBuffer.addAll(igdbService.retrieveGamesByIds(ids.subList(0, limit - 1), limit));
                ids.subList(0, limit - 1).clear();

                logger.info(String.format("Retrieved %1$s games. %2$s games remaining.", limit, remainder));
            } else {
                igdbGameBuffer.addAll(igdbService.retrieveGamesByIds(ids, limit));
                logger.info(String.format("Retrieved %1$s games.", ids.size()));
                ids.clear();

                remainder = ids.size();
                logger.info(String.format("%1$s games remaining.", remainder));
            }

            // Update games collection with new and updated games if greater than or equal to the igdbGameBufferSize
            // Update games collection if remainder is negative, as there are no more games to retrieve.
            if ((limit * numberOfRequestsWithLimit) >= igdbGameBufferSize || remainder <= 0) {
                gameRepository.bulkUpdateGames(igdbGameBuffer);
                int numUpdated = bulkUpdateElasticSearch(igdbGameBuffer);
                updatedCount += numUpdated;

                igdbGameBuffer.clear();
            }

            numberOfRequestsWithLimit++;
        }

        String status = String.format("Finished replicating %s games in the games collection.", updatedCount);
        logger.info(status);

        return status;
    }
}
