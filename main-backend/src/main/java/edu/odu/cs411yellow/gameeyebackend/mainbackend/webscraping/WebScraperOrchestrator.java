package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.*;

@Service
public class WebScraperOrchestrator {

    NewsWebsiteRepository newsWebsiteRepository;

    private final MockNewsScraper mockNewsScraper;
    private final UniversalScraper scraper;

    final private String[] scraperNames = {"GameSpot", "Eurogamer", "PC Gamer", "IGN"};

    @Qualifier("elasticsearchOperations")
    ElasticGameRepository elastic;

    private final ReferenceGameService referenceGameService;
    private final GameService gameService;
    GameRepository games;
    private final MachineLearningService mlService;

    @Autowired
    public WebScraperOrchestrator(UniversalScraper scraper, MockNewsScraper mockNewsScraper, ElasticGameRepository elastic,
                                  ReferenceGameService referenceGameService, NewsWebsiteRepository newsWebsiteRepository,
                                  GameRepository games, GameService gameService, MachineLearningService mlService) {
        this.mockNewsScraper = mockNewsScraper;
        this.elastic = elastic;
        this.referenceGameService = referenceGameService;
        this.mlService = mlService;
        this.newsWebsiteRepository = newsWebsiteRepository;
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

        for (String s : scraperNames) {
            List<Article> articleList = scraper.scrape(s);

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
                List<String> ids = performArticleGameReferenceSearch(article);
                String id = ids.get(ids.size() - 1);

                // Add article to scrapedArticles if not a duplicate in db or scrapedArticles
                if (!checkArticleDuplicates(id, article) && !scrapedArticles.contains(article)) {
                    scrapedArticles.add(article);
                    articleTitles.add(article.getTitle());
                }
            }
        }

        assignScrapedArticlesImportance(articleTitles, scrapedArticles);
        insertArticlesIntoDatabase(scrapedArticles);

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

        assignScrapedArticlesImportance(articleTitles, scrapedArticles);
        insertArticlesIntoDatabase(scrapedArticles);

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

        assignScrapedArticlesImportance(articleTitles, scrapedArticles);
        insertArticlesIntoDatabase(scrapedArticles);

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
