package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.security.IgdbTokenContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
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
                .defaultHeader("Authorization", "Bearer " + token.getAccessToken())
                .build();
    }

    public String getCompanies() {
        return this.webClient.post()
                .uri("/companies")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue("fields name; limit 10;")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public GameResponse getGameResponseById(int id) throws JsonProcessingException {
        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name; ";
        String whereClause = "where id = " + id + ";";

        GameResponse gameResponse = new GameResponse();

        String gameJson = webClient.post()
                .uri("/games")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(fieldsClause + whereClause)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<GameResponse> gameResponseList = new ObjectMapper().readValue(gameJson, new TypeReference<>() {});

        if (gameResponseList.size() != 0) {
            gameResponse = gameResponseList.get(0);
        }

        return gameResponse;
    }

    public List<GameResponse> getGameResponsesByRangeWithLimit(int minId, int maxId, int limit) throws JsonProcessingException {
        int inclusiveMinId = minId - 1;
        int inclusiveMaxId = maxId + 1;

        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name; ";
        String whereClause = "where id > " + inclusiveMinId + " & id < " + inclusiveMaxId + ";";
        String limitClause = "limit " + limit + ";";

        GameResponse gameResponse = new GameResponse();

        String gameJson = webClient.post()
                .uri("/games")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(fieldsClause + whereClause + limitClause)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<GameResponse> gameResponses = new ObjectMapper().readValue(gameJson, new TypeReference<>() {});

        return gameResponses;
    }

    public List<GameResponse> getGameResponsesByRange(int minId, int maxId) throws JsonProcessingException {
        List<GameResponse> games = new ArrayList<>();
        int limit = 250;
        int remainder = maxId - minId + 1;
        int currentMaxId = maxId;
        int currentMinId = minId;

        while (remainder > 0) {
            List<GameResponse> gameResponses;

            if (remainder > limit) {
                currentMaxId = currentMinId + limit;
                remainder -= limit;

                gameResponses = getGameResponsesByRangeWithLimit(currentMinId, currentMaxId, limit);

                logger.info("Retrieved games in ID range " + currentMinId + "-" + currentMaxId);

                currentMinId = currentMaxId + 1;
            } else {
                currentMaxId = maxId;
                gameResponses = getGameResponsesByRangeWithLimit(currentMinId, currentMaxId, limit);

                logger.info("Retrieved games ID range " + currentMinId + "-" + currentMaxId);

                remainder = (remainder - currentMaxId - currentMinId + 1);
            }

            games.addAll(gameResponses);
        }

        return games;
    }

    public List<Game> convertGameResponsesToGames(List<GameResponse> gameResponses) {
        List<Game> games = new ArrayList<>();

        for (GameResponse gameResponse : gameResponses) {
            if (gameResponse.igdbId != "") {
                Game game = gameResponse.toGame();
                games.add(game);
            }
        }

        return games;
    }

    public Game getGameById(int id) throws JsonProcessingException {
        GameResponse gameResponse = getGameResponseById(id);
        Game game = gameResponse.toGame();

        return game;
    }

    public List<Game> getGamesByRangeWithLimit(int minId, int maxId, int limit) throws JsonProcessingException {
        List<GameResponse> gameResponses = getGameResponsesByRangeWithLimit(minId, maxId, limit);
        List<Game> games = convertGameResponsesToGames(gameResponses);

        return games;
    }

    public List<Game> getGamesByRange(int minId, int maxId) throws JsonProcessingException {
        List<GameResponse> gameResponses = getGameResponsesByRange(minId, maxId);
        List<Game> games = convertGameResponsesToGames(gameResponses);

        return games;
    }
}
