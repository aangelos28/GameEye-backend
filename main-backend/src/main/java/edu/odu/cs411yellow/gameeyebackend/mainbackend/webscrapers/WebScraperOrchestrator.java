package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class WebScraperOrchestrator{

    NewsWebsiteRepository newsWebsiteRepository;

    private List<WebScraper> scrapers;

    private List<Article> scrapedArticles;

    private MockNewsScraper mockNewsScraper;

    /*@Autowired
    IGNScraper ignScraper;
    @Autowired
    GameSpotScraper gameSpotScrapper;
    @Autowired
    EuroGamerScraper euroGamerScraper;
    @Autowired
    PCGamerScraper pcGamerScraper;*/

    @Autowired
    public WebScraperOrchestrator (MockNewsScraper mockNewsScraper, NewsWebsiteRepository newsWebsiteRepository){
        this.scrapers = new ArrayList<WebScraper>();
        this.scrapedArticles = new ArrayList<Article>();
        this.mockNewsScraper = mockNewsScraper;
        this.newsWebsiteRepository = newsWebsiteRepository;

        //this.ign = new IGNScraper(newsWebsiteRepository);
        //this.gameSpot= new GameSpotScraper(newsWebsiteRepository);
        //this.euroGamer= new EuroGamerScraper(newsWebsiteRepository);
        //this.pcGamer = new PCGamerScraper(newsWebsiteRepository);
        //this.mockSite = new MockNewsScraper(newsWebsiteRepository);


        /*WebScraper ign = new IGNScraper(newsWebsiteRepository);
        WebScraper gameSpot= new GameSpotScraper(newsWebsiteRepository);
        WebScraper euroGamer= new EuroGamerScraper(newsWebsiteRepository);
        WebScraper pcGamer = new PCGamerScraper(newsWebsiteRepository);
        WebScraper mockSite = new MockNewsScraper(newsWebsiteRepository);*/

        //WebScraper ign = ignScraper;
        //WebScraper gameSpot = gameSpotScrapper;
        //WebScraper euroGamer = euroGamerScraper;
        //WebScraper pcGamer = euroGamerScraper;

        WebScraper mockSite=mockNewsScraper;

        //scrapers.add(ign);
        //scrapers.add(gameSpot);
        //scrapers.add(euroGamer);
        //scrapers.add(pcGamer);
        //scrapers.add(mockNewsScraper);
        scrapers.add(mockSite);
    }


    public WebScraperOrchestrator (WebScraper target){
        this.scrapers = new ArrayList<WebScraper>();
        this.scrapedArticles = new ArrayList<Article>();

        //WebScraper targetScraper = mockNewsScraper;

        /*Boolean init = false;
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
                //targetScraper = new MockNewsScraper(newsWebsiteRepository);
                targetScraper = mockNewsScraper;
                init = true;
                break;
            case "PC Gamer":
                targetScraper = new PCGamerScraper(newsWebsiteRepository);
                init = true;
                break;
            default:
                //targetScraper = new MockNewsScraper(newsWebsiteRepository); //Temporary default
                targetScraper = mockNewsScraper;
        }*/

        /*if(init){
            scrapers.add(targetScraper);
        }
        else{
            throw new IllegalArgumentException("Invalid Scraper called");
        }*/

        //WebScraper mockSite=mockNewsScraper;
        //scrapers.add(targetScraper);

        //scrapers.add(mockNewsScraper);
        //scrapers.add(mockSite);

        scrapers.add(target);
    }

    public void forceScrape(){
        for (WebScraper scraper:scrapers) {
            List<Article> articleList = scraper.scrape();
            //List<Article> articleList = scraper.getArticles();
            for (Article art:articleList) {
                if(!checkArticleDuplicates() && !checkIrrelevantArticles())
                    scrapedArticles.add(art);
            }
        }
    }

    //@Scheduled (cron = "0 0 8,20 * * *")    //Schedules method to run at 8:00 AM and 8:00PM
    public void biDailyScrape(){
        //TODO
    }

    public Boolean checkArticleDuplicates(){

        return false;
    }

    public Boolean checkIrrelevantArticles(){

        return false;
    }

    public void insertDataIntoDatabase(){
        //TODO
    }

    public void performArticleGameReferenceSearch(){
        //TODO
        //Consult Chris
    }

    public void removeFromCollection(){
        //TODO
    }

    public List<Article> getArticleCollection(){ return scrapedArticles; }

    public List<WebScraper> getScrapers(){ return scrapers;}

    public String toString(){
        ObjectMapper obj= new ObjectMapper();
        String scrapedArticlesStr="";
        for (Article a:scrapedArticles) {
            try {
                String temp;
                temp = obj.writerWithDefaultPrettyPrinter().writeValueAsString(a);
                scrapedArticlesStr = scrapedArticlesStr + "\n" + temp;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return scrapedArticlesStr;
    }
}
