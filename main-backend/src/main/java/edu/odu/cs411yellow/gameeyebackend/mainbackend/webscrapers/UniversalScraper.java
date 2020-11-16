package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class UniversalScraper implements WebScraper {

    NewsWebsiteRepository newsWebsites;
    //private List<Article> articles;
    //private DateFormat format;
    //private String name;
    //private String url;

    @Autowired
    public UniversalScraper(NewsWebsiteRepository newsWebsites){
        this.newsWebsites = newsWebsites;
        //articles = new ArrayList<>();
        //format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");
    }

    /**
     * Initiate the scrape
     */
    @Override
    public List<Article> scrape(String newsOutlet) {
        String name = newsOutlet;
        List<Article> articles = new ArrayList<>();
        try {
            NewsWebsite newsSite = newsWebsites.findByName(newsOutlet);

            String url = newsSite.getRssFeedUrl();
            Document rssFeed = Jsoup.connect(url).get();

            Elements items = rssFeed.select("item");

            for (var i : items){
                Article toAdd = createArticle(i,newsSite.getName());
                articles.add(toAdd);
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return articles;
    }

    @Override
    public Article createArticle(Element i, String websiteName) throws ParseException {
        DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");
        String title = i.select("title").text();

        String url = i.select("link").text();

        String snippet;

        //parse date
        String pubDate = i.select("pubDate").text();
        Date publicationDate = format.parse(pubDate);

        //parse snippet
        if (websiteName.contentEquals("IGN")) {
             snippet = i.select("description").text();
        }

        else if (websiteName.contentEquals("PC Gamer")){
            Document body = Jsoup.parse(i.selectFirst("title").nextElementSibling().text());
            Elements paragraph = body.select("p");
            snippet = paragraph.text();
        }

        else {
            Document body = Jsoup.parse(i.select("description").text());
            Elements paragraph = body.select("p");
            snippet = paragraph.text();
        }

        if (snippet.length() > 255){
            snippet = snippet.substring(0,255);
        }

        return new Article("", title, url, websiteName, "",
                snippet, publicationDate, publicationDate, false);

    }

    /*@Override
    public Boolean checkDuplicateArticles(Article a){
        return false;
    }*/

    /**
     * Retrieve articles
     * @return list of articles
     */
    /*@Override
    public List<Article> getArticles() {
        return articles;
    }*/

    /**
     * Retrieve article given index
     * @param index Index pertaining to an article
     * @return article given an index
     */
    /*@Override
    public Article getArticle(int index) {
        return articles.get(index);
    }*/

    /**
     * Retrieves name of the scraper
     *
     * @return String
     */
    /*@Override
    public String getScraperName(){ return name; }*/

    /*@Override
    public void emptyArticles(){
        articles.clear();
    }*/

    /**
     * Output to JSON format
     * @return JSON
     */
    //@Override
    public String toString(String name) {
        ObjectMapper obj= new ObjectMapper();
        String articlesStr="";
        List<Article> articles = scrape(name);
        for (Article a:articles){

            try {
                String temp;
                temp = obj.writerWithDefaultPrettyPrinter().writeValueAsString(a);
                articlesStr = String.format("%1$s\n%2$s", articlesStr, temp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return articlesStr;
    }

}
