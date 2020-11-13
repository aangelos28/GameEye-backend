package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Contains CLI commands related to IGDB data retrieval.
 */
@ShellComponent
public class IgdbDataRetrieval {
    WebClient webClient;

    public IgdbDataRetrieval(@Value("${mainbackend.baseurl}") String igdbUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(igdbUrl)
                .build();
    }

    /**
     * Replicates IGDB game data to GameEye database.
     *
     * @param minId lower IGDB game id.
     * @param maxId higher IGDB game id.
     * @param limit maximum number of games per API request.
     */
    @ShellMethod(value = "Replicate IGDB game data.", key = "replicate-igdb")
    public void replicateIgdb(@ShellOption("--min-id") int minId,
                              @ShellOption("--max-id") int maxId,
                              @ShellOption("--limit")  int limit) {

    JsonObject request = new JsonObject();
    request.addProperty("minId", minId);
    request.addProperty("maxId", maxId);
    request.addProperty("limit", limit);

    String response = this.webClient.post()
            .uri("/private-admin/igdb/replicate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request.toString())
            .retrieve()
            .bodyToMono(String.class)
            .block();

        System.out.println(response);
    }
}
