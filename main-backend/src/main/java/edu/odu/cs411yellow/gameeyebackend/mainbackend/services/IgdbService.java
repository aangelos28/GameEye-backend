package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.CoverResponse;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.IdResponse;



import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.security.IgdbTokenContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

/**
 * Service for calling IGDB REST API.
 */
@Service
public class IgdbService {
    private final WebClient webClient;
    IgdbTokenContainer token;

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

    public GameResponse retrieveGameResponseById(int id) {
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

    public CoverResponse retrieveCoverResponseByGameId(String gameId) {
        String fieldsClause = "fields url, game; ";
        String whereClause = String.format("where game = %1$s;", gameId);
        String limitClause = String.format("limit %s;", 1);
        String requestBody = String.format("%1$s%2$s%3$s", fieldsClause, whereClause, limitClause);

        List<CoverResponse> coverResponses = webClient.post()
                .uri("/covers")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CoverResponse>>() {
                })
                .block();

        return coverResponses.get(0);
    }

    public List<GameResponse> retrieveGameResponsesByIdRange(int minId, int maxId, int limit) {
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

    public List<GameResponse> retrieveGameResponsesByTitles(final List<String> titles, int limit) {
        String names = convertTitlesToIgdbWhereClauseNames(titles);

        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name, first_release_date; ";
        String whereClause = String.format("where name = %1$s;", names);
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

    public List<GameResponse> retrieveGameResponsesByIds(final List<String> ids, int limit) {
        String formattedIds = convertIdsToIgdbWhereClauseGameIds(ids);

        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name, first_release_date; ";
        String whereClause = String.format("where id = %1$s;", formattedIds);
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

    public List<CoverResponse> retrieveCoverResponsesByGameIdRange(int minId, int maxId, int limit) {
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

    public List<CoverResponse> retrieveCoverResponsesByGameIds(List<String> gameIds, int limit) {
        String ids = convertIdsToIgdbWhereClauseGameIds(gameIds);

        String fieldsClause = "fields url, game; ";
        String whereClause = String.format("where game = %1$s; ", ids);
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

    public List<GameResponse> retrieveNewReleaseGameResponses(long releaseDateUpperBound, int limit) {
        String fieldsClause = "fields game, date; ";
        String whereClause = "";

        if (releaseDateUpperBound == 0) {
            whereClause = String.format("where human != \"TBD\";");
        } else {
            whereClause = String.format("where human != \"TBD\" & date < %s;", releaseDateUpperBound);
        }

        String sortClause = String.format("sort date desc;");
        String limitClause = String.format("limit %s;",limit);
        String requestBody = String.format("%1$s%2$s%3$s%4$s", fieldsClause, whereClause, sortClause, limitClause);

        List<IdResponse> idResponses = webClient.post()
                .uri("/release_dates")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<IdResponse>>() {})
                .block();

        List<String> ids = new ArrayList<>();

        for (IdResponse id: idResponses) {
            ids.add(id.getGameId());
        }

        return retrieveGameResponsesByIds(ids, limit);
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

    public Map<String, String> convertCoverResponsesToLogos(List<CoverResponse> coverResponses) {
        Map<String, String> logos = new HashMap<>();

        for (CoverResponse cover: coverResponses) {
            logos.put(cover.gameId, cover.logoUrl);
        }

        return logos;
    }

    public Game retrieveGameById(int id) {
        return new Game(retrieveGameResponseById(id));
    }

    public List<Game> retrieveGamesByTitle(List<String> titles, int limit) {
        List<GameResponse> gameResponses = retrieveGameResponsesByTitles(titles, limit);
        List<String> ids = new ArrayList<>();

        for (GameResponse response: gameResponses) {
            ids.add(response.igdbId);
        }

        List<CoverResponse> coverResponses = retrieveCoverResponsesByGameIds(ids, limit);
        List<Game> games = convertGameResponsesToGames(gameResponses);
        Map<String, String> logos = convertCoverResponsesToLogos(coverResponses);
        addLogosToGames(games, logos);

        return games;
    }

    public List<Game> retrieveNewReleases(long oldestReleaseDate, int limit) {
        List<GameResponse> gameResponses = retrieveNewReleaseGameResponses(oldestReleaseDate, limit);

        List<String> ids = new ArrayList<>();

        for (GameResponse response: gameResponses) {
            ids.add(response.igdbId);
        }

        List<Game> games = convertGameResponsesToGames(gameResponses);
        List<CoverResponse> coverResponses = retrieveCoverResponsesByGameIds(ids, limit);
        Map<String, String> logos = convertCoverResponsesToLogos(coverResponses);
        addLogosToGames(games, logos);

        return games;
    }

    public String convertTitlesToIgdbWhereClauseNames(final List<String> titles) {
        String names = "(";

        Iterator it = titles.iterator();
        while (it.hasNext()) {
            names += "\"" + it.next() + "\"";

            if (it.hasNext()) {
                names += ", ";
            }
        }

        names += ")";

        return names;
    }

    public String convertIdsToIgdbWhereClauseGameIds(final List<String> ids) {
        String names = "(";

        Iterator it = ids.iterator();
        while (it.hasNext()) {
            names += it.next();

            if (it.hasNext()) {
                names += ", ";
            }
        }

        names += ")";

        return names;
    }

    public List<Game> retrieveGamesByRangeWithLimit(int minId, int maxId, int limit) {
        List<GameResponse> gameResponses = retrieveGameResponsesByIdRange(minId, maxId, limit);
        List<CoverResponse> coverResponses = retrieveCoverResponsesByGameIdRange(minId, maxId, limit);

        List<Game> games = convertGameResponsesToGames(gameResponses);
        Map<String, String> logos = convertCoverResponsesToLogos(coverResponses);

        addLogosToGames(games, logos);

        return games;
    }

    public void addLogosToGames(List<Game> games, Map<String, String> logos) {
        // Add logos to games
        for (int i = 0; i < games.size(); i++) {
            String igdbId = games.get(i).getIgdbId();

            if (logos.get(igdbId) != null) {
                games.get(i).setLogoUrl(logos.get(igdbId));
            }
        }
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
