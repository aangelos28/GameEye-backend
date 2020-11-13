package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.MockNewsScraper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.WebScraperOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class WebScrapeController {

    WebScraperOrchestrator webScraperOrchestrator;
    Logger logger = LoggerFactory.getLogger(WebScrapeController.class);

    @Autowired
    MockNewsScraper mock;

    @Autowired
    public WatchlistController (WebScraperOrchestrator webScraperOrchestrator){
        this.webScraperOrchestrator = webScraperOrchestrator;
    }

    /**
     * Perform ForceScrape on RSS feeds
     *
     */
    @GetMapping(path = "/private-admin/webscraping/force", String)
    public ResponseEntity<?> performForceScrapeRSS()) {
        webScraperOrchestrator.forceScrape();

    }

    /**
     * Perform ForceScrape on RSS feeds
     *
     */
    @GetMapping(path = "/private-admin/webscraping/mockwebsite/force", String)
    public ResponseEntity<?> performForceScrapeMockSite()) {
        webScraperOrchestrator.forceScrape(mock);

    }

}
