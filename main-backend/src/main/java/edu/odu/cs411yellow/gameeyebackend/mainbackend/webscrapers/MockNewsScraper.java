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
public class MockNewsScraper implements WebScraper{

    NewsWebsiteRepository newsWebsites;
    private String url;
    private List<Article> articles;
    private DateFormat format;
    private String name="GameEye Mock News";

    @Autowired
    public MockNewsScraper(NewsWebsiteRepository newsWebsites){
        this.newsWebsites = newsWebsites;
        articles = new ArrayList<>();
        url = newsWebsites.findByName(name).getSiteUrl();
        format = new SimpleDateFormat("E, MMMM d, yyyy");
    }

    /**
     * Initiate the scrape
     */
    public List<Article> scrape(String newsWebsite) {

        try {
            NewsWebsite mockNews = newsWebsites.findByName(name);

            Document RssFeed = Jsoup.parse(Jsoup.connect(url).get().select("ul").toString());

            Elements items = RssFeed.select("div");
            for (var i : items){

                Article toAdd = createArticle(i,mockNews.getName());
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


        //Get Info
        String title = i.select("h2").text();

        //Parse Link
        String url = i.select("a").toString();
        String delims = "\"";
        String [] parse = url.split(delims);
        url = "https://gameeye-mock-news.netlify.app" + parse[1];

        String pubDate = i.selectFirst("p").text();
        Date date = format.parse(pubDate);

        String snippet = i.selectFirst("p").nextElementSibling().text();
        if (snippet.length() > 255){
            snippet = snippet.substring(0,255);
        }

        return new Article("", title, url, websiteName, "",
                snippet, date, date, false);

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
     * Retrieves name of the scraper
     *
     * @return String
     */
    @Override
    public String getScraperName(){ return name; }

    @Override
    public void emptyArticles(){
        articles.clear();
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
                articlesStr = String.format("%1$s\n%2$s", articlesStr, temp);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return articlesStr;
    }


}
