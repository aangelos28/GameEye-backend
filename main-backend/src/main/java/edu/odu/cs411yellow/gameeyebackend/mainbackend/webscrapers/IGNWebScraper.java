package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class IGNWebScraper implements WebScraper{

    private String url = "http://feeds.feedburner.com/ign/games-all";
    private List<Article> articles;
    private DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");
    private String siteURL = "https://www.ign.com/";

    @Autowired
    NewsWebsiteRepository siteBuilder;


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

                String id = UUID.randomUUID().toString();

                NewsWebsite ign = siteBuilder.findByName("IGN");
//                NewsWebsite ign = new NewsWebsite(id,"IGN",null,siteURL,url,
//                        pubDate,pubDate);
                //TODO
                //Set article ID
                //
                //Capture article Image
                //
                //Get Impact Score
                //
                //Get Last Published Date
                //
                //Check for duplicates

                int impact = 0;

                Article curr= new Article(id,title, source, ign, null, snippet, pubDate, pubDate, impact);
                articles.add(curr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Article createArticle(Element e, NewsWebsite newsSite){
        return null;
    }

    @Override
    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public Article getArticle(int index){
        return articles.get(index);
    }

    @Override
    public Boolean checkDuplicateArticles(Article a){
        return false;
    }
}
