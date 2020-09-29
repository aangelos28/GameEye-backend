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
                //String title = link.attr("title");
                String title = link.select("title").text();
                //String source = link.attr("href");
                String source = link.select("link").text();

                String pubDate = link.select("pubDate").text();
                Date publicationDate = format.parse(pubDate);


                //Document body = Jsoup.parse(link.select("description").text());
                //Document body = Jsoup.parse(link.select("description").text());
                //Elements paragraph = body.select("p");
                String snippet = link.select("description").text();
                //String snippet = paragraph.text();
                if (snippet.length() > 255)
                    snippet = snippet.substring(0,255);


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
