package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel.GameResponse;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.security.IgdbTokenContainer;
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

    @Autowired
    public IgdbService(WebClient.Builder webClientBuilder, @Value("${igdb.baseurl}") String igdbUrl,
                       IgdbTokenContainer token) {
        this.token = token;
        this.webClient = webClientBuilder
                .baseUrl(igdbUrl)
                .defaultHeader("Client-ID",token.getClientId())
                .defaultHeader("Authorization","Bearer " + token.getAccessToken())
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

    public GameResponse getGameById(int id) throws JsonProcessingException {
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


        System.out.println(gameJson);
        List<GameResponse> gameResponseList = new ObjectMapper().readValue(gameJson, new TypeReference<>() {
        });

        if (gameResponseList.size() != 0) {
            gameResponse = gameResponseList.get(0);
        }

        return gameResponse;

    }

    public List<GameResponse> getGamesByRange(int lowerId, int upperId) throws JsonProcessingException{

        List<GameResponse> gameResponses = new ArrayList<>();
        GameResponse gameResponse = new GameResponse();

        for (int id = lowerId; id < upperId + 1; id++) {
            gameResponse = getGameById(id);

            if (!gameResponse.name.equals("")) {
                gameResponses.add(gameResponse);

            }

        }

        return gameResponses;

    }

    public List<GameResponse> getGamesByOffset(int offset) throws JsonProcessingException {
        List<GameResponse> gameResponses = new ArrayList<>();

        int id = offset;
        /**
         * Tracks number of null responses from IGDB.
         * Null responses may be a sign a random game that has been removed or the end of the database is reached.
         * The threshold is currently set to 100 successive nulls.
         */
        int nullCount = 0;

        while (nullCount != 100) {
            id++;
            GameResponse gameResponse = getGameById(id);
            if (gameResponse.igdbId.equals("")) {
                nullCount++;
            }
            else {
                gameResponses.add(gameResponse);
                nullCount--;
            }

        }

        return gameResponses;
    }

}
