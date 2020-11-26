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
     * Initiate Scraping of RSS of news websites.
     */
    @ShellMethod(value = "Initiate Force Scrape of RSSFeeds.", key = "scrape-all")
    public void scrapeAll() {
        System.out.println("Attempting to scrape new articles for each news website.");
        String response = this.webClient.post()
                .uri("/private-admin/webscraping/all/run")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }

    /**
     * Initiate Scraping of RSS feed of mock news website.
     */
    @ShellMethod(value = "Initiate Force Scrape of MockNews.", key = "scrape-mock-news")
    public void scrapeMockNewsWebsite() {
        System.out.println("Attempting to scrape new articles from the GameEye Mock News website.");
        String response = this.webClient.post()
                .uri("/private-admin/webscraping/mocknewswebsite/run")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response);
    }
}
