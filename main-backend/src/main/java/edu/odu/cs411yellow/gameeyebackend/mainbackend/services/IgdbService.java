package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.GameResponse;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    public GameResponse getGameResponseById(int id) throws JsonProcessingException {
        String fieldsClause = "fields name, updated_at, genres, websites; ";
        String whereClause = "where id = " + id + ";";

        String gameJson = webClient.post()
                .uri("/games")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(fieldsClause + whereClause)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<GameResponse> gameResponseList = new ObjectMapper().readValue(gameJson, new TypeReference<List<GameResponse>>(){});

        GameResponse gameResponse = gameResponseList.get(0);

        return gameResponse;
    }
}
