package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.annotations.NotNull;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;


import java.io.IOException;
import java.util.*;

@Service
public class WebScraperOrchestrator{

    NewsWebsiteRepository newsWebsiteRepository;

    private List<WebScraper> scrapers;
    private List<Article> scrapedArticles;
    private List<Article> allArticles;

    private List<String> articleTitles;

    private MockNewsScraper mockNewsScraper;
    private UniversalScraper scraper;

    private String[] scraperNames={"GameSpot","Eurogamer","PC Gamer","IGN"};

    private ElasticsearchOperations elasticSearch;

    @Qualifier("elasticsearchOperations")
    ElasticGameRepository elastic;

    private ReferenceGameService rgs;
    private GameService gameService;
    GameRepository games;
    private MachineLearningService machine;

    @Autowired
    public WebScraperOrchestrator(UniversalScraper scraper, MockNewsScraper mockNewsScraper, ElasticGameRepository elastic,
                                   ReferenceGameService rgs, NewsWebsiteRepository newsWebsiteRepository, GameRepository games,
                                   GameService gameService, MachineLearningService machine) {
        this.scrapedArticles = new ArrayList<>();
        this.allArticles = new ArrayList<>();
        this.mockNewsScraper = mockNewsScraper;

        this.elastic = elastic;
        this.rgs = rgs;
        this.machine = machine;

        this.newsWebsiteRepository = newsWebsiteRepository;

        this.scraper = scraper;

        this.games=games;
        this.gameService = gameService;

        this.articleTitles = new ArrayList<String>();

    }

    /**
     * Forces a scrape from each scraper, cleans and stores the collected articles,
     * and inserts them into the database
     */
    public void forceScrape(){
        for(String s:scraperNames){
            List<Article> articleList = scraper.scrape(s);

            try{
                for (Article art:articleList) {
                    allArticles.add(art);
                    if (!checkIrrelevantArticles(art)){
                        List<String> ids = performArticleGameReferenceSearch(art);
                        String id = ids.get(ids.size() - 1);

                        // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                        if (!checkArticleDuplicates(id, art) && !scrapedArticles.contains(art)) {
                            scrapedArticles.add(art);
                            articleTitles.add(art.getTitle());
                        }
                    }
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            scraper.emptyArticles();
        }

        List<Article> mockNewsArticles = mockNewsScraper.scrape(mockNewsScraper.getScraperName());
        for (Article art:mockNewsArticles) {
            allArticles.add(art);
            if(!checkIrrelevantArticles(art)){
                List<String> ids = performArticleGameReferenceSearch(art);
                String id = ids.get(ids.size() - 1);

                // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                if (!checkArticleDuplicates(id, art) && !scrapedArticles.contains(art)) {
                    scrapedArticles.add(art);
                    articleTitles.add(art.getTitle());
                }

            }
        }

        assignScrapedArticlesImportance();
        insertArticlesIntoDatabase();
        mockNewsScraper.emptyArticles();
    }

    /**
     * Forces a scrape from a specific scraper, cleans and stores the collected articles,
     * and inserts them into the database
     * @param target String ID for a scraper
     */
    public void forceScrape(String target){
        List<Article> articleList = scraper.scrape(target);

        for(Article art:articleList) {
            allArticles.add(art);
            if (!checkIrrelevantArticles(art)) {
                List<String> ids = performArticleGameReferenceSearch(art);
                String id = ids.get(ids.size() - 1);

                // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                if (!checkArticleDuplicates(id, art) && !scrapedArticles.contains(art)) {
                    scrapedArticles.add(art);
                    articleTitles.add(art.getTitle());
                }

            }
        }

        assignScrapedArticlesImportance();
        insertArticlesIntoDatabase();
        scraper.emptyArticles();
    }

    /**
     * Forces a scrape of the Mock News Website, cleans and stores the collected articles,
     * and inserts them into the database
     * @param target WebScraper object for Mock News Website
     */
    public void forceScrape(WebScraper target){
            List<Article> articleList = mockNewsScraper.scrape("GameEye Mock News");

            for (Article art:articleList) {
                allArticles.add(art);
                if(!checkIrrelevantArticles(art)) {
                    List<String> ids = performArticleGameReferenceSearch(art);
                    String id = ids.get(ids.size() - 1);

                    // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                    if (!checkArticleDuplicates(id, art) && !scrapedArticles.contains(art)) {
                        scrapedArticles.add(art);
                        articleTitles.add(art.getTitle());
                    }
                }
            }

        assignScrapedArticlesImportance();
        insertArticlesIntoDatabase();
        mockNewsScraper.emptyArticles();
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
     * Iterates through a list of ElasticGame ids and removes ids that pertain to games
     * that are not present in the database
     * @param ids
     * @return A list of string ids for games
     */
    public List<String> cleanIds(List<String> ids){
        Iterator<String> iterator = ids.iterator();

        while(iterator.hasNext()){
            String id = iterator.next();

            if(!gameService.existsById(id)){
                iterator.remove();
            }
        }

        return ids;
    }


    /**
     * Checks if the article already exists in the database
     * @param a A scraped article
     * @return Boolean: true if the article already exists
     */
    public Boolean checkArticleDuplicates(Article a){
        Boolean dupe = false;
        List<String> possibleGameIDS = performArticleGameReferenceSearch(a);
        for(String id: possibleGameIDS){

            ElasticGame gameToCheck = elastic.findByGameId(id);
            String title = gameToCheck.getTitle();

            Game gameInDB = games.findByTitle(title);

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
     * #TestMethod
     * Checks if the article already exists in the database
     * @param id String id for a game
     * @param a An article
     * @return Boolean: true if the article already exists
     */
    public Boolean checkArticleDuplicates(String id, Article a){
            Game gameInDB = games.findGameById(id);

            Resources gameResources;
            List<Article> storedGameArticles = new ArrayList<>();

            try{
                gameResources = gameInDB.getResources();
                storedGameArticles = gameResources.getArticles();

            }catch(NullPointerException e){
                //e.printStackTrace();
                //System.out.print()
            }

            for(Article storedArticle:storedGameArticles){
                if(a.equals(storedArticle)){
                    return true;
                }
            }

            return false;
        }


    /**
     * Checks if the article is irrelevant and does not contain a game title
     * @param a A scraped article
     * @return Boolean: true if the article is irrelevant
     */
    public Boolean checkIrrelevantArticles(Article a){
        List<String> possibleGameIDS = performArticleGameReferenceSearch(a);

        for(String id:possibleGameIDS){
            ElasticGame game = elastic.findByGameId(id);
            //System.out.println(game.getTitle());
        }

        if(possibleGameIDS.size()>0)
        {
            return false;
        }

        return true;
    }

    /**
     * Inserts collected relevant, non-duplicate articles into the database
     */
    public void insertArticlesIntoDatabase(){
        for(Article a:scrapedArticles){
           List<String> gameIds = performArticleGameReferenceSearch(a);

           for(String id: gameIds){
               if (games.existsById(id)) {
                   gameService.addArticleToGame(a, id);
               }
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

    /**
     * Gets the importance of each article title from the scraped articles
     * @return A list of Booleans to signify importance
     */
    public List<Boolean> getArticleImportance(){
        return machine.predictArticleImportance(articleTitles);
    }

    /**
     * Gets and sets the importance scores for each scraped article
     */
    public void assignScrapedArticlesImportance(){
        List<Boolean> articlesImportance = getArticleImportance();
        for(int i=0; i < articlesImportance.size(); i++){
            Boolean temp = articlesImportance.get(i);
            scrapedArticles.get(i).setIsImportant(temp);
        }
    }

    /**
     * Gets the importance for each title from a list of titles
     * @param titles An article title
     * @return A list of Booleans to signify importance
     */
    public List<Boolean> getArticleImportance(List<String> titles){
        return machine.predictArticleImportance(titles);
    }

    /**
     * Sets the importance for an article
     * @param important Value that determines importance
     * @param a An article
     */
    public void setArticleImportance(Boolean important, Article a){

           a.setIsImportant(important);

    }

    /**
     * Adds an article to a game's resources
     * @param a An article
     * @param gameId The id of a game in the database
     */
    public void addArticleToGame(Article a, String gameId){
        gameService.addArticleToGame(a, gameId);
    }

    /**
     * Removes an article from the collection of scraped articles
     */
    public void removeFromCollection(Article a){
        scrapedArticles.remove(a);
    }

    /**
     * Getter for collection of cleaned scraped articles
     * @return A List of articles
     */
    public List<Article> getArticleCollection(){ return scrapedArticles; }

    /**
     * Getter for collection of scraped articles
     * @return A List of articles
     */
    public List<Article> getAllArticles(){return allArticles; }

    /**
     * Getter for collection of article titles
     * @return A List of article titles
     */
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
