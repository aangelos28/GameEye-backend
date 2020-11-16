package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Contains CLI commands related to WebScrapeOrchestrator.
 */
@ShellComponent
public class WebScrape {
    WebClient webClient;

    public WebScrape(@Value("${mainbackend.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Initiate Scraping of RSS
     *
     */
    @ShellMethod(value = "Initiate Force Scrape of RSSFeeds.", key = "Force-Scrape-RSS")
    public void forceScrapeAll() {
        webClient.post().uri("/private-admin/webscraping/force");
        String response = "Force Scrape All";
        System.out.println(response);
    }

    /**
     * Initiate Scraping of RSS
     *
     */
    @ShellMethod(value = "Initiate Force Scrape of MockNews.", key = "Force-Scrape-MockNews")
    public void forceScrapeMockNews() {
        webClient.post().uri("/private-admin/webscraping/mockwebsite/force");
        String response = "Force Scrape MockNews";
        System.out.println(response);
    }
}
