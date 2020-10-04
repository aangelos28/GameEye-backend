package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;

import java.text.ParseException;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Common interface for web scrapers to search for news articles and
 * provide a list of articles.
 */
public interface WebScraper {

    /**
     * Initiates web scraping.
     */
    void scrape();

    /**
     * Retrieves a list of the extracted news articles by the web scraper
     *
     * @return A List of Articles
     */
    List<Article> getArticles();

    /**
     * Retrieves a specific news article provided an index.
     *
     * @param index Index pertaining to an article
     * @return Article
     */
    Article getArticle(int index);

    /**
     * Creates an Article object from the extracted url, article title, article
     * publication date, news website, article thumbnail,
     * article description, and calculated impact score
     *
     * @param e HTML element pulled from the RSS feed
     * @param newsSite  Website where the article originated
     * @return  Article
     * @throws ParseException
     */
    Article createArticle(Element e, NewsWebsite newsSite) throws ParseException;

    /**
     * Checks if newly created article object is already present in list of
     * extracted articles
     *
     * @param a Newly created Article
     * @return Boolean
     */
    Boolean checkDuplicateArticles(Article a);
}
