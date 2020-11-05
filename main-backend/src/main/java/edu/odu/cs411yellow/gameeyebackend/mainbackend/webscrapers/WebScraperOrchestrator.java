package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepositoryCustom;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepositoryCustomImpl;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
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
    private List<String> articleTitles;

    private MockNewsScraper mockNewsScraper;
    private UniversalScraper scraper;
    //private String[] scraperNames={"GameSpot","Eurogamer","PC Gamer", "IGN","GameEye Mock News"};
    private String[] scraperNames={"GameSpot","Eurogamer","PC Gamer", "IGN"};

    private ElasticsearchOperations elasticSearch;


    @Qualifier("elasticsearchOperations")
    private ElasticGameRepository elastic;

    private ReferenceGameService rgs;
    private GameRepository games;
    private MachineLearningService machine;


    @Autowired
    public WebScraperOrchestrator (UniversalScraper scraper, MockNewsScraper mockNewsScraper, ElasticGameRepository elastic,
                                   ReferenceGameService rgs, NewsWebsiteRepository newsWebsiteRepository, GameRepository games){
        //this.scrapers = new ArrayList<WebScraper>();
        this.scrapedArticles = new ArrayList<Article>();
        this.mockNewsScraper = mockNewsScraper;

        this.elastic = elastic;
        this.rgs = rgs;
        //this.machine = machine;

        this.newsWebsiteRepository = newsWebsiteRepository;

        this.scraper = scraper;

        this.games=games;

    }

    /**
     * Forces a scrape from each scraper, cleans and stores the collected articles,
     * and inserts them into the database
     */
    public void forceScrape(){

        for(String s:scraperNames){
            List<Article> articleList = scraper.scrape(s);
            for (Article art:articleList) {
                if(!checkIrrelevantArticles(art) && !checkArticleDuplicates(art))
                {
                    scrapedArticles.add(art);
                    articleTitles.add(art.getTitle());
                }
            }
        }
        List<Article> mockNewsArticles = mockNewsScraper.scrape(mockNewsScraper.getScrapperName());

        for (Article art:mockNewsArticles) {
            if(!checkIrrelevantArticles(art) && !checkArticleDuplicates(art))
            {
                scrapedArticles.add(art);
                articleTitles.add(art.getTitle());
            }
        }

        //insertDataIntoDatabase();
    }

    /**
     * Forces a scrape from a specific scraper, cleans and stores the collected articles,
     * and inserts them into the database
     * @param target String ID for a scraper
     */
    public void forceScrape(String target){

        try{
            List<Article> articleList = scraper.scrape(target);
            for (Article art:articleList) {
                if(!checkIrrelevantArticles(art) && !checkArticleDuplicates(art))
                    scrapedArticles.add(art);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        //insertDataIntoDatabase();
    }

    public void forceScrape(WebScraper target){


            List<Article> articleList = mockNewsScraper.scrape("GameEye Mock News");
            for (Article art:articleList) {
                if(!checkIrrelevantArticles(art) && !checkArticleDuplicates(art))
                    scrapedArticles.add(art);
            }


        //insertDataIntoDatabase();
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
        Boolean dupe = false;
        List<String> possibleGameIDS = performArticleGameReferenceSearch(a);
        for(String id: possibleGameIDS){

            //Game gameToCheck = games.findGameById(id);
            ElasticGame gameToCheck = elastic.findByGameId(id);
            String title = gameToCheck.getTitle();
            Game gameInDB;

            /*if (!games.existsByTitle(title)) {
                addGameToDB(gameToCheck);
            }*/

            gameInDB = games.findByTitle(title);

            Resources gameResources;
            List<Article> storedGameArticles = new ArrayList<>();

            try{
                gameResources = gameInDB.getResources();
                storedGameArticles = gameResources.getArticles();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            for(Article storedArticle:storedGameArticles){
                if(a.equals(storedArticle)){
                    dupe=true;
                }
            }
        }

        return dupe;
    }

    /**
     * Checks if the article is irrelevant and does not contain a game title
     * @param a A scraped article
     * @return Boolean
     */
    public Boolean checkIrrelevantArticles(Article a){
        List<String> possibleGameIDS = performArticleGameReferenceSearch(a);
        if(possibleGameIDS.size()>0)
        {
            return false;
        }

        return true;
    }

    /**
     * Inserts collected, clean articles into the database
     */
    public void insertArticlesIntoDatabase(){
        for(Article a:scrapedArticles){
           List<String> gameIds = performArticleGameReferenceSearch(a);

           for(String id: gameIds){
               ElasticGame gameInElasticRepo = elastic.findByGameId(id);
               String title = gameInElasticRepo.getTitle();
               Game gameInDB = games.findByTitle(title);

               gameInDB.addArticleResources(a);
           }

        }
    }

    /**
     * Performs an Elastic Search on article titles to find the games an article
     * refers to
     * @param a A scraped article
     * @return List<String>  List of IDs for games present in game article
     */
    public List<String> performArticleGameReferenceSearch(Article a){

        //List<String> ids = elastic.referencedGames(a);
        List<String> ids = rgs.getReferencedGames(a);

        return ids;
    }

    public List<Boolean> getArticleImportance(){
        return machine.predictArticleImportance(articleTitles);
    }

    public void setArticleImportance(Boolean important, Article a){

           a.setIsImportant(important);

    }

    /**
     * Adds a game to the GameEye database
     * @param game  game to be added
     */
    public void addGameToDB(ElasticGame game){
        String title = game.getTitle();
        String mongoID = game.getGameId();
        String elasticID = game.getId();

        Game newGame = new Game();

        //games.save(newGame);
    }

    /**
     * Removes an article from the collection of scraped articles
     */
    public void removeFromCollection(Article a){
        scrapedArticles.remove(a);
    }

    /**
     * Getter for collection of scraped articles
     * @return A List of articles
     */
    public List<Article> getArticleCollection(){ return scrapedArticles; }

    public List<String> getArticleTitles(){ return articleTitles; }

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
