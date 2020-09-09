package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import java.util.List;

/**
 * Common interface for web scrapers to search for news articles and
 * provide a list of articles.
 */
public interface WebScraper {

    /**
     * Initiates web scraping
     */
    void scrape();

    /**
     * Retrieves a list of the extracted news articles by the web scraper
     * @return  A List of Articles
     */
    List<Article> getList();

    /**
     * Retrieves a specific news article provided an index
     * @param index Index pertaining to an article
     * @return  An Article
     */
    Article getArticle(int index);

}
