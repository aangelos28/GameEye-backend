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

    public int findMaxGameId() {
        String fieldsClause = "fields id; ";
        String sortClause = "sort id desc; ";
        String limitClause = "limit 1; ";

        String requestBody = String.format("%1$s%2$s%3$s", fieldsClause, sortClause, limitClause);

        List<GameResponse> gameResponse = webClient.post()
                .uri("/covers")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GameResponse>>() {})
                .block();

        return Integer.valueOf(gameResponse.get(0).igdbId);
    }
}
