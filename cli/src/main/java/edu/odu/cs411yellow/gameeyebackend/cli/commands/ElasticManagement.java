package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Contains CLI commands related to Elasticsearch database administration.
 */
@ShellComponent
public class ElasticManagement {
    private WebClient webClient;

    public ElasticManagement(@Value("${mainbackend.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Populates the Elasticsearch database from game data from the main backend database.
     * Recreates the Elasticsearch games index, if necessary.
     */
    @ShellMethod(value = "Populates the Elasticsearch database from the backend game database.", key = "populate-elastic")
    public void populateElasticsearch() {
        System.out.println("Attempting to create the Elasticsearch database.");

        String response = this.webClient.post()
                .uri("/private-admin/elasticgames/populate")
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }
}
