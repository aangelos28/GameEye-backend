package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.util.List;

/**
 * Common interface for web scrapers to search for news articles and
 * provide a list of articles.
 */
public interface WebScraper {
    /**
     * Initiates web scraping.
     *
     * @return List of Articles
     */
    List<Article> scrape(String newsOutlet);

    /**
     * Creates an Article object from the extracted url, article title, article
     * publication date, news website, article thumbnail,
     * article description, and calculated impact score
     *
     * @param e               HTML element pulled from the RSS feed
     * @param newsWebsiteName Website where the article originated
     * @return Article
     * @throws ParseException If article could not be parsed
     */
    Article createArticle(Element e, String newsWebsiteName) throws ParseException;
}
