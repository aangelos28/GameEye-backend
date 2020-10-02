package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.GameResponse;
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

    public IgdbService(WebClient.Builder webClientBuilder, @Value("${igdb.baseurl}") String igdbUrl, @Value("${apikeys.igdb}") String igdbKey) {
        this.webClient = webClientBuilder
                .baseUrl(igdbUrl)
                .defaultHeader("user-key", igdbKey)
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

    public GameResponse getGameResponseById(String id) throws JsonProcessingException {
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


        List<GameResponse> gameResponseList = new ObjectMapper().readValue(gameJson, new TypeReference<List<GameResponse>>(){});

        if (gameResponseList.size() != 0) {
            gameResponse = gameResponseList.get(0);
        }

        return gameResponse;

    }

    public List<GameResponse> getGameResponsesByRange(int lowerId, int upperId) throws JsonProcessingException, InterruptedException {
        String fieldsClause = "fields name, updated_at, genres.name, websites.url," +
                              "websites.category, platforms.name; ";

        List<GameResponse> gameResponses = new ArrayList<>();
        GameResponse gameResponse = new GameResponse();

        for (int id = lowerId; id < upperId + 1; id++) {
            gameResponse = getGameResponseById(String.valueOf(id));
            Thread.sleep(250);

            if (gameResponse.name != "") {
                gameResponses.add(gameResponse);

            }

        }

        return gameResponses;

    }

}
