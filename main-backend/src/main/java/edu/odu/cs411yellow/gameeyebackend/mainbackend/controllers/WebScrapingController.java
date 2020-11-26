package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping.UniversalScraper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping.WebScraperOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebScrapingController {

    WebScraperOrchestrator webScraperOrchestrator;

    UniversalScraper universalScraper;

    @Autowired
    public WebScrapingController (WebScraperOrchestrator webScraperOrchestrator,
                                UniversalScraper universalScraper){
        this.webScraperOrchestrator = webScraperOrchestrator;
        this.universalScraper = universalScraper;
    }

    /**
     * Perform ForceScrape on All Sources
     */
    @PostMapping(path = "/private-admin/webscraping/all/run")
    public ResponseEntity<String> scrapeAll() {
        try {
            int numArticles = webScraperOrchestrator.scrapeAll().size();
            return ResponseEntity.ok(String.format("Finished scraping %s new articles.", numArticles));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAILED scraping all resources");
        }
    }

    /**
     * Perform ForceScrape on mocknewsSite
     */
    @PostMapping(path = "/private-admin/webscraping/mocknewswebsite/run")
    public ResponseEntity<?> scrapeMockNewsWebsite() {
        try {
            int numArticles = webScraperOrchestrator.scrape(universalScraper).size();
            return ResponseEntity.ok(String.format("Finished scraping %s new articles from GameEye Mock News.", numArticles));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAILED scraping the mock news website");
        }
    }

}
