package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Contains CLI commands related to WebScrapeOrchestrator.
 */
@ShellComponent
public class WebScraping {
    WebClient webClient;

    public WebScraping(@Value("${mainbackend.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Initiate Scraping of RSS
     */
    @ShellMethod(value = "Initiate Force Scrape of RSSFeeds.")
    public void scrapeAll() {
        String response = this.webClient.post()
                .uri("/private-admin/webscraping/all/run")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }

    /**
     * Initiate Scraping of RSS
     */
    @ShellMethod(value = "Initiate Force Scrape of MockNews.")
    public void scrapeMockNewsWebsite() {
        String response = this.webClient.post()
                .uri("/private-admin/webscraping/mocknewswebsite/run")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }
}
