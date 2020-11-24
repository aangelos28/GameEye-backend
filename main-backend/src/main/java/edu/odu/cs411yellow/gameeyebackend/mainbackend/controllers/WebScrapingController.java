package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping.UniversalScraper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping.WebScraperOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
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
            webScraperOrchestrator.scrapeAll();
            return ResponseEntity.ok("Finished scraping all resources");
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("FAILED scraping all resources");
        }
    }

    /**
     * Perform ForceScrape on mocknewsSite
     */
    @PostMapping(path = "/private-admin/webscraping/mocknewswebsite/run")
    public ResponseEntity<?> scrapeMockNewsWebsite() {
        try {
            webScraperOrchestrator.scrape(universalScraper);
            return ResponseEntity.ok("Finished scraping the mock news website");
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("FAILED scraping the mock news website");
        }
    }

}
