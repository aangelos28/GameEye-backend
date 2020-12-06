package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Contains CLI commands related to game data in the MongoDB database.
 */
@ShellComponent
public class GameManagement {
    WebClient webClient;

    public GameManagement(@Value("${mainbackend.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Removes articles from a game indicated by a game id.
     *
     * @param id id of the game for which articles will be removed.
     */
    @ShellMethod(value = "Remove articles from a game indicated by id.", key = "remove-articles-by-id")
    public void removeArticlesFromGameById(@ShellOption("--id") String id) {
        System.out.println(String.format("Attempting to remove articles from game with id of %s.", id));

        String response = this.webClient.delete()
                .uri(String.format("/private-admin/games/articles/delete/id/%s", id))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }

    /**
     * Removes articles from all games with the indicated game title.
     *
     * @param title title of the game(s) for which articles will be removed.
     */
    @ShellMethod(value = "Remove articles from a game indicated by title.", key = "remove-articles-by-title")
    public void removeArticlesFromGameByTitle(@ShellOption("--title") String title) {
        System.out.println(String.format("Attempting to remove articles from game(s) with title of %s.", title));

        String response = this.webClient.delete()
                .uri(String.format("/private-admin/games/articles/delete/title/%s", title))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }

    /**
     * Removes all articles from each game in the games collection.
     */
    @ShellMethod(value = "Remove all articles from every game.", key = "remove-all-articles")
    public void removeAllArticles() {
        System.out.println("Attempting to remove all articles from every game in the games collection.");

        String response = this.webClient.delete()
                .uri("/private-admin/games/articles/delete/all")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }
}
