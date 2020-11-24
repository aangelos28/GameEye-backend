package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping.UniversalScraper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping.WebScraperOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebScrapeController {

    WebScraperOrchestrator webScraperOrchestrator;

    UniversalScraper mock;

    @Autowired
    public WebScrapeController (WebScraperOrchestrator webScraperOrchestrator,
                                UniversalScraper mock){
        this.webScraperOrchestrator = webScraperOrchestrator;
        this.mock = mock;
    }

    /**
     * Perform ForceScrape on All Sources
     */
    @PostMapping(path = "/private-admin/webscraping/force")
    public ResponseEntity<String> performForceScrapeAll() {
        try {
            webScraperOrchestrator.scrapeAll();
            return ResponseEntity.ok("Force Scrape of RSS feeds Performed");
        }
        catch(Exception e){
//            System.out.println(e);
            return ResponseEntity.ok("Force Scrape of RSS feeds FAILED");
        }
    }

    /**
     * Perform ForceScrape on mocknewsSite
     */
    @PostMapping(path = "/private-admin/webscraping/mockwebsite/force")
    public ResponseEntity<?> performForceScrapeMockSite() {
        try {
            webScraperOrchestrator.scrape(mock);
            return ResponseEntity.ok("Force Scrape of Mock News Performed");
        }
        catch(Exception e){
//            System.out.println(e);
            return ResponseEntity.ok("Force Scrape of Mock News FAILED");
        }
    }

}
