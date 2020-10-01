package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    public IgdbModel.GameResponse getGameResponseById(int id) {
            String fieldsClause = "fields name, updated_at, genres, websites; ";
            String whereClause = "where id = " + id + ";";

        String game = webClient.post()
                .uri("/games")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(fieldsClause + whereClause)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(game);

            IgdbModel.GameResponse gameResponse[] = webClient.post()
                    .uri("/games")
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValue(fieldsClause + whereClause)
                    .retrieve()
                    .bodyToMono(IgdbModel.GameResponse[].class)
                    .block();


        return gameResponse[]
    }
}
