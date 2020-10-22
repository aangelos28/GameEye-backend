package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import  edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.IGNScraper;
import  edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.EuroGamerScraper;
import  edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.GameSpotScraper;
import  edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.MockNewsScraper;
import  edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers.PCGamerScraper;

import java.util.List;
import java.util.ArrayList;



public class WebScraperOrchestrator {

    NewsWebsiteRepository newsWebsiteRepository;
    List<Object> scrapers = new ArrayList<Object>();

    @Autowired
    public WebScraperOrchestrator(){

        IGNScraper ign = new IGNScraper(newsWebsiteRepository);
        GameSpotScraper gameSpot= new GameSpotScraper(newsWebsiteRepository);
        EuroGamerScraper euroGamer= new EuroGamerScraper(newsWebsiteRepository);
        PCGamerScraper pcGamer = new PCGamerScraper(newsWebsiteRepository);
        MockNewsScraper mockSite = new MockNewsScraper(newsWebsiteRepository);

        scrapers.add(ign);
        scrapers.add(gameSpot);
        scrapers.add(euroGamer);
        scrapers.add(pcGamer);
        scrapers.add(mockSite);
    }

    public void forceScrape(){
        //TODO
    }

    public void initiateBiDailyScrape(){
        //TODO
    }

    public Boolean checkArticleDuplicates(){
        return false;
    }

    public void insertDataIntoDatabase(){
        //TODO
    }

    public void performArticleGameReferenceSearch(){
        //TODO
        //Consult Chris
    }
}
