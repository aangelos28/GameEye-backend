package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.CompanyResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.CoverResponse;

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
        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name; ";
        String whereClause = String.format("where id = %s;", id);
        String requestBody = String.format("%1$s%2$s", fieldsClause, whereClause);

        // IGDB always returns an array of JSON objects.
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

        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name; ";
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

    public List<GameResponse> getGameResponsesWithMultipleRequests(int minId, int maxId, int limit) {
        List<GameResponse> games = new ArrayList<>();
        int remainder = maxId - minId + 1;
        int currentMaxId = maxId;
        int currentMinId = minId;

        while (remainder > 0) {
            List<GameResponse> gameResponses;

            if (remainder > limit) {
                currentMaxId = currentMinId + limit;
                remainder -= limit;

                gameResponses = getGameResponsesWithSingleRequest(currentMinId, currentMaxId, limit);

                logger.info("Retrieved games in ID range " + currentMinId + "-" + currentMaxId + ".");

                currentMinId = currentMaxId + 1;
            } else {
                currentMaxId = maxId;
                gameResponses = getGameResponsesWithSingleRequest(currentMinId, currentMaxId, limit);

                logger.info("Retrieved games in ID range " + currentMinId + "-" + currentMaxId + ".");

                remainder = (remainder - currentMaxId - currentMinId + 1);
            }

            games.addAll(gameResponses);
        }

        return games;
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

    public List<CoverResponse> getCoverResponsesWithMultipleRequests(int minId, int maxId, int limit) {
        List<CoverResponse> covers = new ArrayList<>();
        int remainder = maxId - minId + 1;
        int currentMaxId = maxId;
        int currentMinId = minId;

        while (remainder > 0) {
            List<CoverResponse> coverResponses;

            if (remainder > limit) {
                currentMaxId = currentMinId + limit;
                remainder -= limit;

                coverResponses = getCoverResponsesWithSingleRequest(currentMinId, currentMaxId, limit);

                logger.info("Retrieved covers for games in ID range " + currentMinId + "-" + currentMaxId + ".");

                currentMinId = currentMaxId + 1;
            } else {
                currentMaxId = maxId;
                coverResponses = getCoverResponsesWithSingleRequest(currentMinId, currentMaxId, limit);

                logger.info("Retrieved covers for games in ID range " + currentMinId + "-" + currentMaxId + ".");

                remainder = (remainder - currentMaxId - currentMinId + 1);
            }

            covers.addAll(coverResponses);
        }

        return covers;
    }

    public List<Game> convertGameResponsesToGames(List<GameResponse> gameResponses) {
        List<Game> games = new ArrayList<>();

        for (GameResponse gameResponse : gameResponses) {
            if (!gameResponse.igdbId.equals("")) {
                Game game = gameResponse.toGame();

                games.add(game);
            }
        }

        return games;
    }

    public Game getGameById(int id) {
        GameResponse gameResponse = getGameResponseById(id);
        Game game = gameResponse.toGame();

        return game;
    }

    public List<Game> retrieveGamesByRangeWithLimit(int minId, int maxId, int limit) {
        List<GameResponse> gameResponses = getGameResponsesWithMultipleRequests(minId, maxId, limit);
        List<CoverResponse> coverResponses = getCoverResponsesWithMultipleRequests(minId, maxId, limit);

        List<Game> games = convertGameResponsesToGames(gameResponses);

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
}
