package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.NotificationService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.*;

@Service
public class WebScraperOrchestrator {

    private final MockNewsScraper mockNewsScraper;
    private final UniversalScraper scraper;

    private final String[] scraperNames = {"GameSpot", "Eurogamer", "PC Gamer", "IGN"};

    private final ReferenceGameService referenceGameService;
    private final GameService gameService;
    private final MachineLearningService mlService;
    private final NotificationService notificationService;

    private final GameRepository games;

    @Autowired
    public WebScraperOrchestrator(UniversalScraper scraper, MockNewsScraper mockNewsScraper, ReferenceGameService referenceGameService,
                                  GameRepository games, GameService gameService, MachineLearningService mlService,
                                  NotificationService notificationService) {
        this.mockNewsScraper = mockNewsScraper;
        this.referenceGameService = referenceGameService;
        this.mlService = mlService;
        this.notificationService = notificationService;
        this.scraper = scraper;
        this.games = games;
        this.gameService = gameService;
    }

    /**
     * Forces a scrape from each scraper, cleans and stores the collected articles,
     * and inserts them into the database
     */
    public List<Article> scrapeAll() {
        List<Article> scrapedArticles = new ArrayList<>();
        List<String> articleTitles = new ArrayList<>();
        List<String> articleGameIds = new ArrayList<>();

        for (String scraper : scraperNames) {
            List<Article> articleList = this.scraper.scrape(scraper);

            try {
                for (Article article : articleList) {
                    if (!checkIrrelevantArticles(article)) {
                        List<String> ids = performArticleGameReferenceSearch(article);
                        String id = ids.get(ids.size() - 1);

                        // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                        if (!checkArticleDuplicates(id, article) && !scrapedArticles.contains(article)) {
                            scrapedArticles.add(article);
                            articleTitles.add(article.getTitle());
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        List<Article> mockNewsArticles = mockNewsScraper.scrape(mockNewsScraper.getScraperName());
        for (Article article : mockNewsArticles) {
            if (!checkIrrelevantArticles(article)) {
                List<String> gameIds = performArticleGameReferenceSearch(article);
                String gameId = gameIds.get(gameIds.size() - 1);

                // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                if (!checkArticleDuplicates(gameId, article) && !scrapedArticles.contains(article)) {
                    scrapedArticles.add(article);
                    articleTitles.add(article.getTitle());
                    articleGameIds.add(gameId);
                }
            }
        }

        if (!scrapedArticles.isEmpty()) {
            assignScrapedArticlesImportance(articleTitles, scrapedArticles);
            insertArticlesIntoDatabase(scrapedArticles);

            // Send article notifications
            notificationService.sendArticleNotificationsAsync(scrapedArticles, articleGameIds);
        }

        return scrapedArticles;
    }

    /**
     * Forces a scrape from a specific scraper, cleans and stores the collected articles,
     * and inserts them into the database
     *
     * @param target String ID for a scraper
     */
    public List<Article> scrape(String target) {
        List<Article> articleList = scraper.scrape(target);
        List<Article> scrapedArticles = new ArrayList<>();
        List<String> articleTitles = new ArrayList<>();
        List<String> articleGameIds = new ArrayList<>();

        for (Article article : articleList) {
            if (!checkIrrelevantArticles(article)) {
                List<String> gameIds = performArticleGameReferenceSearch(article);
                String gameId = gameIds.get(gameIds.size() - 1);

                // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                if (!checkArticleDuplicates(gameId, article) && !scrapedArticles.contains(article)) {
                    scrapedArticles.add(article);
                    articleTitles.add(article.getTitle());
                    articleGameIds.add(gameId);
                }

            }
        }

        if (!scrapedArticles.isEmpty()) {
            assignScrapedArticlesImportance(articleTitles, scrapedArticles);
            insertArticlesIntoDatabase(scrapedArticles);

            // Send article notifications
            notificationService.sendArticleNotificationsAsync(scrapedArticles, articleGameIds);
        }

        return scrapedArticles;
    }

    /**
     * Forces a scrape of the Mock News Website, cleans and stores the collected articles,
     * and inserts them into the database
     *
     * @param target WebScraper object for Mock News Website
     */
    public List<Article> scrape(WebScraper target) {
        List<Article> articleList = mockNewsScraper.scrape("GameEye Mock News");
        List<Article> scrapedArticles = new ArrayList<>();
        List<String> articleTitles = new ArrayList<>();
        List<String> articleGameIds = new ArrayList<>();

        for (Article article : articleList) {
            if (!checkIrrelevantArticles(article)) {
                List<String> gameIds = performArticleGameReferenceSearch(article);
                String gameId = gameIds.get(gameIds.size() - 1);

                // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                if (!checkArticleDuplicates(gameId, article) && !scrapedArticles.contains(article)) {
                    scrapedArticles.add(article);
                    articleTitles.add(article.getTitle());
                    articleGameIds.add(gameId);
                }
            }
        }

        if (!scrapedArticles.isEmpty()) {
            assignScrapedArticlesImportance(articleTitles, scrapedArticles);
            insertArticlesIntoDatabase(scrapedArticles);

            // Send article notifications
            notificationService.sendArticleNotificationsAsync(scrapedArticles, articleGameIds);
        }

        return scrapedArticles;
    }

    /**
     * Forces a scrape from each scraper, cleans and stores the collected articles,
     * and inserts them into the database twice a day at 8:00AM and 8:00PM
     */
    @Scheduled(cron = "0 0 8,20 * * *")    //Schedules method to run at 8:00 AM and 8:00PM
    public void biDailyScrape() {
        scrapeAll();
    }

    /**
     * Checks if the article already exists in the database
     *
     * @param id      String id for article game
     * @param article An article
     * @return Boolean: true if the article already exists
     */
    public Boolean checkArticleDuplicates(String id, Article article) {
        System.out.println("Game id is " + id);

        Game gameInDB = games.findGameById(id);

        Resources gameResources;
        List<Article> storedGameArticles = new ArrayList<>();

        try {
            gameResources = gameInDB.getResources();
            storedGameArticles = gameResources.getArticles();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        for (Article storedArticle : storedGameArticles) {
            if (article.getTitle().equals(storedArticle.getTitle()) &&
                    article.getNewsWebsiteName().equals(storedArticle.getNewsWebsiteName())) {
                return true;
            }
        }

        return false;
    }


    /**
     * Checks if the article is irrelevant and does not contain a game title
     *
     * @param a A scraped article
     * @return Boolean: true if the article is irrelevant
     */
    public Boolean checkIrrelevantArticles(Article a) {
        List<String> possibleGameIds = performArticleGameReferenceSearch(a);

        return possibleGameIds.size() <= 0;
    }

    /**
     * Inserts collected relevant, non-duplicate articles into the database
     */
    public void insertArticlesIntoDatabase(List<Article> scrapedArticles) {
        for (Article article : scrapedArticles) {
            List<String> gameIds = performArticleGameReferenceSearch(article);

            for (String id : gameIds) {
                if (games.existsById(id)) {
                    gameService.addArticleToGame(article, id);
                }
            }
        }
    }

    /**
     * Performs an Elastic Search on article titles to find the games an article
     * refers to
     *
     * @param a A scraped article
     * @return List<String>  List of IDs for games present in game article
     */
    public List<String> performArticleGameReferenceSearch(Article a) {
        return referenceGameService.getReferencedGames(a);
    }

    /**
     * Gets and sets the importance scores for each scraped article
     */
    public void assignScrapedArticlesImportance(List<String> titles, List<Article> scrapedArticles) {
        List<Boolean> articlesImportance = getArticleImportance(titles);

        for (int i = 0; i < articlesImportance.size(); i++) {
            Boolean temp = articlesImportance.get(i);
            scrapedArticles.get(i).setIsImportant(temp);
        }
    }

    /**
     * Gets the importance for each title from a list of titles
     *
     * @param titles An article title
     * @return A list of Booleans to signify importance
     */
    public List<Boolean> getArticleImportance(List<String> titles) {
        return mlService.predictArticleImportance(titles);
    }

    /**
     * Prints all collected articles in JSON
     *
     * @return JSON output of collected articles
     */
    public String toString() {
        ObjectMapper obj = new ObjectMapper();
        StringBuilder scrapedArticlesStr = new StringBuilder();
        List<Article> scrapedArticles = scrapeAll();

        for (Article article : scrapedArticles) {
            try {
                String temp;
                temp = obj.writerWithDefaultPrettyPrinter().writeValueAsString(article);
                scrapedArticlesStr.append("\n").append(temp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return scrapedArticlesStr.toString();
    }
}
