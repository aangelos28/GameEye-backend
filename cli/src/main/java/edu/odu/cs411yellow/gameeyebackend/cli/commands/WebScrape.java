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
    public WebScrape(@Value("${mainbackend.baseurl}") String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }
    //change ${mainbackend.igdb.baseurl} to http://localhost.4200 in application properties
    //Give everyone updated Application Properties

    /**
     * Initiate Scraping of RSS
     *
     */
    @ShellMethod(value = "Initiate Force Scrape of RSSFeeds.", key = "Force-Scrape-RSS")
    public void forceScrapeRSS() {

        String response = "Force Scrape RSS";
        System.out.println(response);
    }

    /**
     * Initiate Scraping of RSS
     *
     */
    @ShellMethod(value = "Initiate Force Scrape of MockNews.", key = "Force-Scrape-MockNews")
    public void forceScrapeMockNews() {

        String response = "Force Scrape MockNews";
        System.out.println(response);
    }
}
