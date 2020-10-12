package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class IGNScraper implements WebScraper {
    NewsWebsiteRepository newsWebsites;

    //private String url = newsWebsites.findByName("IGN").getRssFeedUrl();
    private List<Article> articles;
    String url;
    private DateFormat format;

    @Autowired
    public IGNScraper(NewsWebsiteRepository newsWebsites) {
        this.articles = new ArrayList<Article>();
        this.url = newsWebsites.findByName("IGN").getSiteUrl();
        this.format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");
    }

    /**
     * Initiates scrape
     *
     * @return List of scraped articles
     */
    @Override
    public List<Article> scrape() {
        try {
            NewsWebsite ign = newsWebsites.findByName("IGN");

            //Connects to RSS feed and parses into a document to retrieve article elements
            Document rssFeed = Jsoup.connect(this.url).get();

            Elements links = rssFeed.getElementsByTag("item");  //A collection of articles from the parsed URL

            //Searches through each individual article
            for (Element link : links) {
                Article curr = createArticle(link, ign);

                //Adds article to collection if not already present in database
                //to prevent duplicate articles
                if (!checkDuplicateArticles(curr)) {
                    articles.add(curr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    /**
     * Creates an Article object from the extracted url, article title, article
     * publication date, news website, article thumbnail,
     * article description, and calculated impact score
     *
     * @param e        HTML element pulled from the RSS feed
     * @param newsSite Website where the article originated
     * @return Article
     * @throws ParseException
     */
    @Override
    public Article createArticle(Element e, NewsWebsite newsSite) throws ParseException {

        String title = e.select("title").text();
        String source = e.select("link").text();
        String publicationDate = e.select("pubDate").text();
        Date pubDate = format.parse(publicationDate);

        //TODO
        //Get Last Published Date
        //String lastUpdated=e.select("").text();
        Date lastPubDate = pubDate;

        //Gets a short description of the article for viewing
        String snippet = e.select("description").text();
        if (snippet.length() > 255) {
            snippet = snippet.substring(0, 255);
        }

        //Placeholder
        int impact = 0;

        //Placeholder
        String id = null; // Assigns a random ID number for article

        //Placeholder
        Image thumbnail = new Image(id, ".jpg", null);

        //TODO
        //Capture article Image
        //
        //Get Impact Score


        return new Article(newsSite.getId(), title, source, newsSite, thumbnail,
                snippet, pubDate, lastPubDate, impact);
    }

    /**
     * Retrieves a list of the extracted news articles by the web scraper
     *
     * @return List of scraped articles
     */
    @Override
    public List<Article> getArticles() {
        return articles;
    }

    /**
     * Retrieves a specific news article provided an index.
     *
     * @param index Index pertaining to an article
     * @return Article
     */
    @Override
    public Article getArticle(int index) {
        return articles.get(index);
    }

    /**
     * Checks if discovered article is already present in database
     *
     * @param a Newly created Article
     * @return Boolean
     */
    @Override
    public Boolean checkDuplicateArticles(Article a) {
        //TODO
        //Check database for article

        return false;
    }

    /**
     * Output to JSON format
     *
     * @return JSON
     */
    @Override
    public String toString() {
        ObjectMapper obj = new ObjectMapper();
        String articlesStr = "";
        for (Article a : articles) {

            try {
                String temp;
                temp = obj.writerWithDefaultPrettyPrinter().writeValueAsString(a);
                articlesStr = articlesStr + "\n" + temp;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return articlesStr;
    }
}
