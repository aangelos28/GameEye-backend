package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IGNWebScraper implements WebScraper{

    private String url = "http://feeds.feedburner.com/ign/games-all";
    private List<Article> articles;
    private DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");

    //private NewsWebsite ign = new NewsWebsite();

    public IGNWebScraper(List<Article> articles) {
        this.articles = articles;
    }


    @Override
    public void scrape()
    {
        try {
            Document doc = Jsoup.connect(url).get();

            Elements links = doc.getElementsByTag("item");

            for(Element link:links)
            {
                String title = link.select("title").text();
                String source = link.select("link").text();

                String publicationDate = link.select("pubDate").text();
                Date pubDate = format.parse(publicationDate);

                String snippet = link.select("description").text();
                if (snippet.length() > 255)
                    snippet = snippet.substring(0,255);

                //TODO
                //Set article ID
                //
                //Capture article Image
                //
                //Get Impact Score
                //
                //Get Last Published Date
                //
                //Article curr= new Article(ID,title, source, ign, IMAGE, snippet, pubDate, LASTUPDATED, IMPACT);
                //articles.add(curr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Article> getArticles() {
        return articles;
    }

    public Article getArticle(int index){
        return articles.get(index);
    }
}
