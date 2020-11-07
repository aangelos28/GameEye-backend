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

    public IgdbDataRetrieval(@Value("${mainbackend.igdb.baseurl}") String igdbUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(igdbUrl)
                .build();
    }

    /**
     * Replicates a range of IGDB games by id to the GameEye database.
     *
     * @param minId lower IGDB game id.
     * @param maxId higher IGDB game id.
     * @param limit maximum number of games per API request.
     */
    @ShellMethod(value = "Replicate a range of games by id.", key = "replicate-by-id-range")
    public void replicateByIdRange(@ShellOption("--min-id") int minId,
                              @ShellOption("--max-id") int maxId,
                              @ShellOption("--limit")  int limit) {

    JsonObject request = new JsonObject();
    request.addProperty("minId", minId);
    request.addProperty("maxId", maxId);
    request.addProperty("limit", limit);

    String response = this.webClient.post()
            .uri("/private-admin/igdb/replicate/ids")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request.toString())
            .retrieve()
            .bodyToMono(String.class)
            .block();

        System.out.println(response);
    }

    /**
     * Replicates a single IGDB game by title to the GameEye database.
     *
     * @param title title of the IGDB game.
     */
    @ShellMethod(value = "Replicate a single game by title.", key = "replicate-by-title")
    public void replicateByTitle(@ShellOption("--title") String title) {

        JsonObject request = new JsonObject();
        request.addProperty("title", title);

        String response = this.webClient.post()
                .uri("/private-admin/igdb/replicate/title")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }
}
