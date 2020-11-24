package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping.UniversalScraper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping.WebScraperOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebScrapeController {

    @Autowired
    WebScraperOrchestrator webScraperOrchestrator;

    @Autowired
    UniversalScraper mock;

    @Autowired
    public WebScrapeController (WebScraperOrchestrator webScraperOrchestrator){
        this.webScraperOrchestrator = webScraperOrchestrator;
    }

    /**
     * Perform ForceScrape on All Sources
     */
    @PostMapping(path = "/private-admin/webscraping/force")
    public ResponseEntity<?> performForceScrapeAll() {
        webScraperOrchestrator.scrapeAll();
        return ResponseEntity.ok("Force Scrape of RSS feeds Performed");
    }

    /**
     * Perform ForceScrape on mocknewsSite
     */
    @PostMapping(path = "/private-admin/webscraping/mockwebsite/force")
    public ResponseEntity<?> performForceScrapeMockSite() {
        webScraperOrchestrator.scrape(mock);
        return ResponseEntity.ok("Force Scrape of Mock News Performed");
    }

}
