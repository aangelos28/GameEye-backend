package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import java.util.List;

/**
 * @author Chris
 */

public class GameSpotWebScraper implements WebScraper {

    private String url = "https://www.gamespot.com/feeds/game-news/";
    private List<Article> articles;
//    try {
//        Document RssFeed = Jsoup.connect(url).userAgent("GameSpot RSS").get();
//    }
//    catch(Exception ex){
//        ex.printStackTrace();
//    }


    /**
     * Constructor
     * @param articles from feed
     */
    public GameSpotWebScraper(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Initiate the scrape
     */
    @Override
    public void scrape() {
        try {
            Document RssFeed = (Document) Jsoup.connect(url).userAgent("GameSpot RSS").get();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * Retrieve articles
     * @return list of articles
     */
    @Override
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Retrieve article given index
     * @param index Index pertaining to an article
     * @return article given an index
     */
    @Override
    public Article getArticle(int index) {
        return null;
    }
}
