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
public class GameSpotScraper implements WebScraper {

    NewsWebsiteRepository newsWebsites;
    private List<Article> articles;
    String url;
    private DateFormat format;

    @Autowired
    public GameSpotScraper(NewsWebsiteRepository newsWebsites){
        this.newsWebsites = newsWebsites;
        articles = new ArrayList<>();
        url= newsWebsites.findByName("GameSpot").getRssFeedUrl();
        format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");

    }

    /**
     * Initiate the scrape
     */
    @Override
    public List<Article> scrape() {

        try {
            NewsWebsite gameSpot = newsWebsites.findByName("GameSpot");

            Document rssFeed = Jsoup.connect(url).get();

            Elements items = rssFeed.select("item");

            for (var i : items){

                Article toAdd = createArticle(i,gameSpot);
                articles.add(toAdd);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return articles;
    }

    @Override
    public Article createArticle(Element i, NewsWebsite site) throws ParseException {
        String title = i.select("title").text();

        String url = i.select("link").text();

        //parse date
        String pubDate = i.select("pubDate").text();
        Date publicationDate = format.parse(pubDate);

        //parse snippet
        Document body = Jsoup.parse(i.select("description").text());
        Elements paragraph = body.select("p");
        String snippet = paragraph.text();
        if (snippet.length() > 255){
            snippet = snippet.substring(0,255);
        }

        Image image = new Image(null, ".jpg",null);

        //Create a Unique ID
        return new Article(null, title, url, site, image,
                snippet, publicationDate, publicationDate, 0);

    }

    @Override
    public Boolean checkDuplicateArticles(Article a){
        return false;
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
        return articles.get(index);
    }

    /**
     * Output to JSON format
     * @return JSON
     */
    @Override
    public String toString() {
        ObjectMapper obj= new ObjectMapper();
        String articlesStr="";
        for (Article a:articles){

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
