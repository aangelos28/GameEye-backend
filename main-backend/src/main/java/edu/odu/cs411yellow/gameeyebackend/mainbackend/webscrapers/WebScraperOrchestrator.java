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
    private GameSpotScraper gameSpotScraper;
    private EuroGamerScraper euroGamerScraper;
    private PCGamerScraper pcGamerScraper;

    @Autowired
    public WebScraperOrchestrator (MockNewsScraper mockNewsScraper, GameSpotScraper gameSpotScraper,
                                   IGNScraper ignScrapper, EuroGamerScraper euroGamerScraper,
                                   PCGamerScraper pcGamerScraper, NewsWebsiteRepository newsWebsiteRepository){
        this.scrapers = new ArrayList<WebScraper>();
        this.scrapedArticles = new ArrayList<Article>();

        this.mockNewsScraper = mockNewsScraper;
        this.gameSpotScraper = gameSpotScraper;
        this.ignScraper = ignScrapper;
        this.euroGamerScraper = euroGamerScraper;
        this.pcGamerScraper = pcGamerScraper;
        this.newsWebsiteRepository = newsWebsiteRepository;


        scrapers.add(ignScrapper);
        scrapers.add(gameSpotScraper);
        scrapers.add(euroGamerScraper);
        scrapers.add(pcGamerScraper);
        scrapers.add(mockNewsScraper);
    }


    /**
     * Forces a scrape from each scraper, cleans and stores the collected articles,
     * and inserts them into the database
     */
    public void forceScrape(){
        for (WebScraper scraper:scrapers) {
            List<Article> articleList = scraper.scrape();
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
        for (WebScraper scraper:scrapers) {
            if(scraper.getScrapperName().equals(target)) {
                List<Article> articleList = scraper.scrape();
                for (Article art : articleList) {
                    if (!checkArticleDuplicates(art) && !checkIrrelevantArticles(art))
                        scrapedArticles.add(art);
                }
            }
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
