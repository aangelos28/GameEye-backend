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
        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name, first_release_date, summary; ";
        String whereClause = String.format("where id = %s;", id);
        String requestBody = String.format("%1$s%2$s", fieldsClause, whereClause);

        List<GameResponse> responses = webClient.post()
                .uri("/games")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GameResponse>>() {})
                .block();

        GameResponse response = new GameResponse();

        try {
            response = responses.get(0);
        } catch (NullPointerException n) {
            System.out.println(String.format("IGDB game with id %s is not found.", id));
        }

        return response;
    }

    public CoverResponse retrieveCoverResponseByGameId(String gameId) {
        String fieldsClause = "fields url, game; ";
        String whereClause = String.format("where game = %1$s;", gameId);
        String limitClause = String.format("limit %s;", 1);
        String requestBody = String.format("%1$s%2$s%3$s", fieldsClause, whereClause, limitClause);

        List<CoverResponse> responses = webClient.post()
                .uri("/covers")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CoverResponse>>() {
                })
                .block();

        CoverResponse response = new CoverResponse();

        try {
            response = responses.get(0);
        } catch (NullPointerException n) {
            System.out.println(String.format("Cover image for IGDB game with id %s is not found.", gameId));
        }

        return response;
    }

    public List<GameResponse> retrieveGameResponsesByIdRange(int minId, int maxId, int limit) {
        int inclusiveMinId = minId - 1;
        int inclusiveMaxId = maxId + 1;

        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name, first_release_date, summary; ";
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

        removeInvalidGames(gameResponses);

        return gameResponses;
    }

    public List<GameResponse> retrieveGameResponsesByTitles(final List<String> titles, int limit) {
        String names = convertTitlesToIgdbWhereClauseNames(titles);

        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name, first_release_date, summary; ";
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

        removeInvalidGames(gameResponses);

        return gameResponses;
    }

    public List<GameResponse> retrieveGameResponsesByIds(final List<String> ids, int limit) {
        String formattedIds = convertIdsToIgdbWhereClauseGameIds(ids);

        String fieldsClause = "fields name, updated_at, genres.name, websites.url, websites.category, platforms.name, first_release_date, summary; ";
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

        removeInvalidGames(gameResponses);

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
        String whereClause;

        if (releaseDateUpperBound == 0) {
            whereClause = "where human != \"TBD\";";
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

        Set<String> ids = new HashSet<>();

        for (IdResponse id: idResponses) {
            ids.add(id.getGameId());
        }

        List<String> uniqueIds = new ArrayList<>(ids);

        return retrieveGameResponsesByIds(uniqueIds, limit);
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

    public List<Game> retrieveGamesByTitles(List<String> titles, int limit) {
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

    public List<Game> retrieveGamesByIds(List<String> ids, int limit) {
        List<GameResponse> gameResponses = retrieveGameResponsesByIds(ids, limit);

        List<CoverResponse> coverResponses = retrieveCoverResponsesByGameIds(ids, limit);
        List<Game> games = convertGameResponsesToGames(gameResponses);
        Map<String, String> logos = convertCoverResponsesToLogos(coverResponses);
        addLogosToGames(games, logos);

        return games;
    }

    public List<Game> retrieveNewReleases(long oldestReleaseDate, int limit) {
        List<GameResponse> gameResponses = retrieveNewReleaseGameResponses(oldestReleaseDate, limit);

        Set<String> ids = new HashSet<>();

        for (GameResponse response: gameResponses) {
            ids.add(response.igdbId);
        }

        List<String> uniqueIds = new ArrayList<>(ids);

        List<Game> games = convertGameResponsesToGames(gameResponses);
        List<CoverResponse> coverResponses = retrieveCoverResponsesByGameIds(uniqueIds, limit);
        Map<String, String> logos = convertCoverResponsesToLogos(coverResponses);
        addLogosToGames(games, logos);

        return games;
    }

    public String convertTitlesToIgdbWhereClauseNames(final List<String> titles) {
        StringBuilder names = new StringBuilder();
        names.append("(");

        Iterator it = titles.iterator();
        while (it.hasNext()) {
            names.append("\"").append(it.next()).append("\"");

            if (it.hasNext()) {
                names.append(", ");
            }
        }

        names.append(")");

        return names.toString();
    }

    public String convertIdsToIgdbWhereClauseGameIds(final List<String> ids) {
        StringBuilder formattedIds = new StringBuilder();
        formattedIds.append("(");

        Iterator it = ids.iterator();
        while (it.hasNext()) {
            formattedIds.append(it.next());

            if (it.hasNext()) {
                formattedIds.append(", ");
            }
        }

        formattedIds.append(")");

        return formattedIds.toString();
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

        List<GameResponse> gameResponses = webClient.post()
                .uri("/games")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GameResponse>>() {})
                .block();

        GameResponse response = new GameResponse();

        try {
            response = gameResponses.get(0);
        } catch (NullPointerException n) {
            n.printStackTrace();
        }

        return Integer.parseInt(response.igdbId);
    }

    public void removeInvalidGames(List<GameResponse> responses) {
        responses.removeIf(response -> response.firstReleaseDateInSeconds == 0
                || response.summary.isEmpty());
    }
}
