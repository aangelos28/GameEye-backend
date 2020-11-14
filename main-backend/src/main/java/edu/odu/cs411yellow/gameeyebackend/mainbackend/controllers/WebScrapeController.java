package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.MockNewsScraper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.WebScraperOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebScrapeController {

    WebScraperOrchestrator webScraperOrchestrator;
    Logger logger = LoggerFactory.getLogger(WebScrapeController.class);

    @Autowired
    MockNewsScraper mock;

    @Autowired
    public WebScrapeController (WebScraperOrchestrator webScraperOrchestrator){
        this.webScraperOrchestrator = webScraperOrchestrator;
    }

    /**
     * Perform ForceScrape on RSS feeds
     *
     */
    @GetMapping(path = "/private-admin/webscraping/force")
    public ResponseEntity<?> performForceScrapeRSS() {
        webScraperOrchestrator.forceScrape();
        return ResponseEntity.ok("Force Scrape of RSS feeds Performed");
    }

    /**
     * Perform ForceScrape on mocknewsSite
     *
     */
    @GetMapping(path = "/private-admin/webscraping/mockwebsite/force")
    public ResponseEntity<?> performForceScrapeMockSite() {
        webScraperOrchestrator.forceScrape(mock);
        return ResponseEntity.ok("Force Scrape of Mock News Performed");
    }

}
