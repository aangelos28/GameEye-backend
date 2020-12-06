package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.odu.cs411yellow.gameeyebackend.cli.model.GameTitles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;

/**
 * Contains CLI commands related to IGDB data retrieval.
 */
@ShellComponent
public class IgdbDataRetrieval {
    WebClient webClient;

    public IgdbDataRetrieval(@Value("${mainbackend.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
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
        System.out.println(String.format("Attempting to replicate IGDB game range %1$s-%2$s with API limit %3$s",
                           minId, maxId, limit));

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
     * Replicates multiple IGDB games by titles to the GameEye database.
     *
     * @param file input file containing a list of titles formatted in JSON.
     * @param limit limit maximum number of games per API request.
     */
    @ShellMethod(value = "Replicate a set of games by titles.", key = "replicate-by-titles")
    public void replicateByTitles(@ShellOption("--file") String file,
                                  @ShellOption("--limit") int limit) throws IOException {
        File inputFile = new File(file);
        ObjectMapper mapper = new ObjectMapper();
        GameTitles titles = mapper.readValue(inputFile, GameTitles.class);

        System.out.println(String.format("Attempting to replicate %1$s games with API limit %2$s.", titles.titles.size(), limit));

        JsonObject request = new JsonObject();
        JsonArray titleArray = new JsonArray();

        for (int i = 0; i < titles.getTitles().size(); i++) {
            titleArray.add(titles.getTitles().get(i));
        }

        request.add("titles", titleArray);
        request.addProperty("limit", limit);

        String response = this.webClient.post()
                .uri("/private-admin/igdb/replicate/titles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }

    /**
     * Updates all games in the games collection of the GameEye database.
     *
     * @param limit maximum number of games per API request.
     */
    @ShellMethod(value = "Update all games in the games collection.", key = "update-games")
    public void updateGamesCollection(@ShellOption("--limit")  int limit) {
        System.out.println(String.format("Attempting to update the games collection with API limit %s.", limit));

        JsonObject request = new JsonObject();
        request.addProperty("limit", limit);

        String response = this.webClient.post()
                .uri("/private-admin/igdb/replicate/update")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }

    /**
     * Replicates new game releases from IGDB.
     * @param num   number of new game releases to replicate.
     * @param limit maximum number of games per API request.
     */
    @ShellMethod(value = "Replicates new game releases from IGDB.", key = "replicate-new-releases")
    public void replicateNewReleases(@ShellOption("--num") int num,
                                     @ShellOption("--limit")  int limit) {
        System.out.println(String.format("Attempting to replicate %1$s new releases with API limit %2$s.", num, limit));

        JsonObject request = new JsonObject();
        request.addProperty("newReleases", num);
        request.addProperty("limit", limit);

        String response = this.webClient.post()
                .uri("/private-admin/igdb/replicate/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }
}
