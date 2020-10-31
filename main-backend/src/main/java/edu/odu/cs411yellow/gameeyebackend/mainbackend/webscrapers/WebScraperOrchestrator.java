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
    private UniversalScraper scrappy;
    private String[] scraperNames={"GameSpot","Eurogamer","PC Gamer", "IGN","GameEye Mock News"};

    //private IGNScraper ignScraper;
    //private GameSpotScraper gameSpotScraper;
    //private EuroGamerScraper euroGamerScraper;
    //private PCGamerScraper pcGamerScraper;

    @Autowired
    public WebScraperOrchestrator (UniversalScraper scrappy, MockNewsScraper mockNewsScraper, NewsWebsiteRepository newsWebsiteRepository){
        this.scrapers = new ArrayList<WebScraper>();
        this.scrapedArticles = new ArrayList<Article>();
        this.mockNewsScraper = mockNewsScraper;


        this.newsWebsiteRepository = newsWebsiteRepository;

        this.scrappy = scrappy;

    }

    /**
     * Forces a scrape from each scraper, cleans and stores the collected articles,
     * and inserts them into the database
     */
    public void forceScrape(){

        for(String scraper:scraperNames){
            List<Article> articleList = scrappy.scrape(scraper);
            for (Article art:articleList) {
                if(!checkArticleDuplicates(art) && !checkIrrelevantArticles(art))
                    scrapedArticles.add(art);
            }
        }

        insertDataIntoDatabase();
    }

    /**
     * Forces a scrape from a specific scraper, cleans and stores the collected articles,
     * and inserts them into the database
     * @param target String ID for a scraper
     */
    public void forceScrape(String target){

        try{
            List<Article> articleList = scrappy.scrape(target);
            for (Article art:articleList) {
                if(!checkArticleDuplicates(art) && !checkIrrelevantArticles(art))
                    scrapedArticles.add(art);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        insertDataIntoDatabase();
    }

    public void forceScrape(WebScraper target){


            List<Article> articleList = mockNewsScraper.scrape("GameEye Mock News");
            for (Article art:articleList) {
                if(!checkArticleDuplicates(art) && !checkIrrelevantArticles(art))
                    scrapedArticles.add(art);
            }


        insertDataIntoDatabase();
    }

    /**
     * Forces a scrape from each scraper, cleans and stores the collected articles,
     * and inserts them into the database twice a day at 8:00AM and 8:00PM
     */
    @Scheduled (cron = "0 0 8,20 * * *")    //Schedules method to run at 8:00 AM and 8:00PM
    public void biDailyScrape(){
        //TODO
    }

    /**
     * Checks if the article already exists in the database
     * @param a A scraped article
     * @return Boolean
     */
    public Boolean checkArticleDuplicates(Article a){
        //TODO
        return false;
    }

    /**
     * Checks if the article is irrelevant and does not contain a game title
     * @param a A scraped article
     * @return Boolean
     */
    public Boolean checkIrrelevantArticles(Article a){
        //TODO
        return false;
    }

    /**
     * Inserts collected, clean articles into the database
     */
    public void insertDataIntoDatabase(){
        //TODO
    }

    /**
     * Performs an Elastic Search on article titles to find the game an article
     * refers to
     * @param a A scraped article
     * @return Int ID for the game
     */
    public int performArticleGameReferenceSearch(Article a){
        //TODO
        //Consult Chris
        return 0;
    }

    /**
     * Removes an article from the collection of scraped articles
     */
    public void removeFromCollection(){
        //TODO
    }

    /**
     * Getter for collection of scraped articles
     * @return A List of articles
     */
    public List<Article> getArticleCollection(){ return scrapedArticles; }

    /**
     * Prints all collected articles in JSON
     * @return JSON output of collected articles
     */
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
