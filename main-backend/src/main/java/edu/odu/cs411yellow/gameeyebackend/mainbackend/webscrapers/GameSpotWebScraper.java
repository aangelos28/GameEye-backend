package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

public class GameSpotWebScraper implements WebScraper {

    private String url = "https://www.gamespot.com/feeds/game-news/";
    private List<Article> articles;
    private DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");



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
            Document feed = Jsoup.connect(url).get();

            Elements items = feed.select("item");

            for (var i : items){

                String title = i.select("title").text();
                String url = i.select("link").text();

                //parse date
                String pubDate = i.select("pubDate").text();
                Date publicationDate = format.parse(pubDate);

                //parse snippet
                Document body = Jsoup.parse(i.select("description").text());
                Elements paragraph = body.select("p");
                String snippet = paragraph.text();
                if (snippet.length() > 255)
                    snippet = snippet.substring(0,255);

                //TODO Create List of Articles

//                Article toAdd = new Article(0, title, url, null,
//                        null, snippet, publicationDate, publicationDate, 0);
//                articles.add(toAdd);

            }
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
        return articles.get(index);
    }
}
