package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


@Component
public class WebScraperOrchestrator{

    NewsWebsiteRepository newsWebsiteRepository;
    List<WebScraper> scrapers = new ArrayList<WebScraper>();

    //WebScraper ign;
    //WebScraper gameSpot;
    //WebScraper euroGamer;
    //WebScraper pcGamer;
    //WebScraper mockSite;


    @Autowired
    public WebScraperOrchestrator(){

        //this.ign = new IGNScraper(newsWebsiteRepository);
        //this.gameSpot= new GameSpotScraper(newsWebsiteRepository);
        //this.euroGamer= new EuroGamerScraper(newsWebsiteRepository);
        //this.pcGamer = new PCGamerScraper(newsWebsiteRepository);
        //this.mockSite = new MockNewsScraper(newsWebsiteRepository);

        WebScraper ign = new IGNScraper(newsWebsiteRepository);
        WebScraper gameSpot= new GameSpotScraper(newsWebsiteRepository);
        WebScraper euroGamer= new EuroGamerScraper(newsWebsiteRepository);
        WebScraper pcGamer = new PCGamerScraper(newsWebsiteRepository);
        WebScraper mockSite = new MockNewsScraper(newsWebsiteRepository);

        scrapers.add(ign);
        scrapers.add(gameSpot);
        scrapers.add(euroGamer);
        scrapers.add(pcGamer);
        scrapers.add(mockSite);
    }

    public WebScraperOrchestrator (String target){
        WebScraper targetScraper = null;
        Boolean init = false;
        switch (target){
            case "IGN":
                targetScraper = new IGNScraper(newsWebsiteRepository);
                init = true;
                break;
            case "GameSpot":
                targetScraper = new GameSpotScraper(newsWebsiteRepository);
                init = true;
                break;
            case "Eurogamer":
                targetScraper = new EuroGamerScraper(newsWebsiteRepository);
                init = true;
                break;
            case "GameEye Mock News":
                targetScraper = new MockNewsScraper(newsWebsiteRepository);
                init = true;
                break;
            case "PC Gamer":
                targetScraper = new PCGamerScraper(newsWebsiteRepository);
                init = true;
                break;
            default:
        }

        if(init){
            scrapers.add(targetScraper);
        }
        else{
            throw new IllegalArgumentException("Invalid Scraper");
        }


    }

    public void forceScrape(){
        for (WebScraper scraper:scrapers) {
            scraper.scrape();
        }

    }

    @Scheduled (cron = "0 0 8,20 * * *")    //Schedules method to run at 8:00 AM and 8:00PM
    public void initiateBiDailyScrape(){
        //TODO
    }

    @Autowired
    public Boolean checkArticleDuplicates(){

        return false;
    }

    @Autowired
    public Boolean checkIrrelevantArticles(){

        return false;
    }

    @Autowired
    public void insertDataIntoDatabase(){
        //TODO
    }

    @Autowired
    public void performArticleGameReferenceSearch(){
        //TODO
        //Consult Chris
    }

    @Autowired
    public void removeFromCollection(){
        //TODO
    }
}
