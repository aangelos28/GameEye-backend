package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
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
    private IGNScraper ignScraper;
    private GameSpotScraper gameSpotScrapper;
    private EuroGamerScraper euroGamerScraper;
    private PCGamerScraper pcGamerScraper;

    @Autowired
    public WebScraperOrchestrator (MockNewsScraper mockNewsScraper, NewsWebsiteRepository newsWebsiteRepository){
        this.scrapers = new ArrayList<WebScraper>();
        this.scrapedArticles = new ArrayList<Article>();

        this.mockNewsScraper = mockNewsScraper;
        this.newsWebsiteRepository = newsWebsiteRepository;


        //scrapers.add(ign);
        //scrapers.add(gameSpot);
        //scrapers.add(euroGamer);
        //scrapers.add(pcGamer);
        scrapers.add(mockNewsScraper);

    }


    public void forceScrape(){
        for (WebScraper scraper:scrapers) {
            List<Article> articleList = scraper.scrape();
            for (Article art:articleList) {
                if(!checkArticleDuplicates() && !checkIrrelevantArticles())
                    scrapedArticles.add(art);
            }
        }
    }

    @Scheduled (cron = "0 0 8,20 * * *")    //Schedules method to run at 8:00 AM and 8:00PM
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
