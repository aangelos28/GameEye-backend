package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.CompanyResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.CoverResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.FindMaxIdResponse;


import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.security.IgdbTokenContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service for calling IGDB REST API.
 */
@Service
public class IgdbService {
    private final WebClient webClient;
    IgdbTokenContainer token;

    Logger logger = LoggerFactory.getLogger(IgdbService.class);

    @Autowired
    public IgdbService(WebClient.Builder webClientBuilder, @Value("${igdb.baseurl}") String igdbUrl,
                       IgdbTokenContainer token) {
        this.token = token;
        this.webClient = webClientBuilder
                .baseUrl(igdbUrl)
                .defaultHeader("Client-ID", token.getClientId())
                .defaultHeader("Authorization", String.format("Bearer %s", token.getAccessToken()))
                .build();
    }

    public List<CompanyResponse> getCompanies() {
        return this.webClient.post()
                .uri("/companies")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("fields name; limit 10;")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CompanyResponse>>() {})
                .block();
    }

    public GameResponse getGameResponseById(int id) {
        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name, first_release_date; ";
        String whereClause = String.format("where id = %s;", id);
        String requestBody = String.format("%1$s%2$s", fieldsClause, whereClause);

        List<GameResponse> gameResponses = webClient.post()
                .uri("/games")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GameResponse>>() {})
                .block();

        // Return game response in gameResponses list.
        return gameResponses.get(0);
    }

    public List<GameResponse> getGameResponsesWithSingleRequest(int minId, int maxId, int limit) {
        int inclusiveMinId = minId - 1;
        int inclusiveMaxId = maxId + 1;

        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name, first_release_date; ";
        String whereClause = String.format("where id > %1$s & id < %2$s;", inclusiveMinId, inclusiveMaxId);
        String limitClause = String.format("limit %s;",limit);
        String requestBody = String.format("%1$s%2$s%3$s", fieldsClause, whereClause, limitClause);

        List<GameResponse> gameResponses = webClient.post()
                .uri("/games")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GameResponse>>() {})
                .block();

        return gameResponses;
    }

    public List<CoverResponse> getCoverResponsesWithSingleRequest(int minId, int maxId, int limit) {
        int inclusiveMinId = minId - 1;
        int inclusiveMaxId = maxId + 1;

        String fieldsClause = "fields url, game; ";
        String whereClause = String.format("where game > %1$s & game < %2$s;", inclusiveMinId, inclusiveMaxId);
        String limitClause = String.format("limit %s;",limit);
        String requestBody = String.format("%1$s%2$s%3$s", fieldsClause, whereClause, limitClause);

        List<CoverResponse> coverResponses = webClient.post()
                .uri("/covers")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CoverResponse>>() {})
                .block();

        return coverResponses;
    }

    public List<Game> convertGameResponsesToGames(List<GameResponse> gameResponses) {
        List<Game> games = new ArrayList<>();

        for (GameResponse gameResponse : gameResponses) {
            if (!gameResponse.igdbId.equals("")) {
                Game game = new Game(gameResponse);

                games.add(game);
            }
        }

        return games;
    }

    public Game getGameById(int id) {
        return new Game(getGameResponseById(id));
    }

    public List<Game> retrieveGamesByRangeWithLimit(int minId, int maxId, int limit) {
        List<GameResponse> gameResponses = getGameResponsesWithSingleRequest(minId, maxId, limit);
        List<CoverResponse> coverResponses = getCoverResponsesWithSingleRequest(minId, maxId, limit);

        List<Game> games = convertGameResponsesToGames(gameResponses);

        // Add logos to games
        for (Game game: games) {
            for (CoverResponse cover: coverResponses) {
                if (game.getIgdbId().equals(cover.gameId)) {
                    game.setLogoUrl(cover.logoUrl);
                    break;
                }
            }
        }

        return games;
    }

    public int findMaxGameId(int requestRateLimitPerSecond, int nullResponseThreshold, int numDaysToBacktrack) throws InterruptedException {
        // Start with initialTimestamp and decrement by day until valid response is returned
        int approxMostRecentGameId = findMostRecentIdByDecrementingDateCreated(requestRateLimitPerSecond, numDaysToBacktrack);

        System.out.println(approxMostRecentGameId);

        // Search for valid games by incrementing from valid id until nullResponseThreshold is exceeded
        int maxId = findMaxIdByIncrementingId(approxMostRecentGameId, nullResponseThreshold, requestRateLimitPerSecond);

        return maxId;
    }

    private int findMostRecentIdByDecrementingDateCreated(int requestRateLimitPerSecond, int numDaysToBacktrack) throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        long initialTimestamp = calendar.getTimeInMillis() / 1000;
        int oneDayInSeconds = 60 * 60 * 24;
        int mostRecentGameId = 0;

        // Start with initialTimestamp and backtrack by one day until valid response
        boolean isValidResponse = false;
        long currentGameTimestamp = initialTimestamp;
        long previousRequestTimestamp = 0;
        while (!isValidResponse) {
            String fieldsClause = "fields id, created_at; ";
            String whereClause = String.format("where created_at > %s;", currentGameTimestamp);
            String requestBody = String.format("%1$s%2$s", fieldsClause, whereClause);

            long differenceBetweenRequests;
            long nextRequestTimestamp = calendar.getTimeInMillis();
            differenceBetweenRequests = nextRequestTimestamp - previousRequestTimestamp;
            if (differenceBetweenRequests < (1000 / requestRateLimitPerSecond)) {
                Thread.sleep(Math.abs((1000 / requestRateLimitPerSecond) - differenceBetweenRequests));
            }

            logger.info(String.format("Attempting to find game created after %s.", (new Date(currentGameTimestamp * 1000)).toString()));
            List<FindMaxIdResponse> response = webClient.post()
                    .uri("/games")
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<FindMaxIdResponse>>() {})
                    .block();

            previousRequestTimestamp = calendar.getTimeInMillis() / 1000;

            if (!response.isEmpty()) {
                mostRecentGameId = response.get(0).id;
                logger.info(String.format("Current max id is %s.", mostRecentGameId));
                isValidResponse = true;
            }
            else {
                currentGameTimestamp = currentGameTimestamp - (oneDayInSeconds * numDaysToBacktrack);
            }
        }

        return mostRecentGameId;
    }

    private int findMaxIdByIncrementingId(int approxMaxId, int nullResponseThreshold, int requestRateLimitPerSecond) throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        long previousRequestTimestamp = 0;
        int consecutiveNullResponses = 0;
        int maxId = approxMaxId;
        int currentMaxId = approxMaxId;
        while (consecutiveNullResponses < nullResponseThreshold) {
            String fieldsClause = "fields id, created_at; ";
            String whereClause = String.format("where id = %s;", currentMaxId);
            String requestBody = String.format("%1$s%2$s", fieldsClause, whereClause);

            long differenceBetweenRequests;
            long nextRequestTimestamp = calendar.getTimeInMillis();
            differenceBetweenRequests = nextRequestTimestamp - previousRequestTimestamp;
            if (differenceBetweenRequests < (1000 / requestRateLimitPerSecond)) {
                Thread.sleep(Math.abs((1000 / requestRateLimitPerSecond) - differenceBetweenRequests));
            }

            logger.info(String.format("Attempting to find game with id of %s.", currentMaxId));
            List<FindMaxIdResponse> response = webClient.post()
                    .uri("/games")
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<FindMaxIdResponse>>() {})
                    .block();

            previousRequestTimestamp = calendar.getTimeInMillis();

            if (!response.isEmpty()) {
                maxId = response.get(0).id;
                logger.info(String.format("Max id is %s.", maxId));
                consecutiveNullResponses = 0;
            }
            else {
                consecutiveNullResponses++;
            }

            currentMaxId++;
        }

        return maxId;
    }
}
