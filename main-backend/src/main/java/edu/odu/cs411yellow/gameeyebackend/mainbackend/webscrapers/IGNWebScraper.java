package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class IGNWebScraper implements WebScraper{

    private String url = "http://feeds.feedburner.com/ign/games-all";
    private List<Article> articles;
    private DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");
    private String siteURL = "https://www.ign.com/";


    public IGNWebScraper(List<Article> articles) {
        this.articles = articles;
    }


    @Override
    public void scrape()
    {
        try {
            //Connects to RSS feed and parses into a document to retrieve article elements
            Document doc = Jsoup.connect(url).get();
            Date buildDate = format.parse(doc.selectFirst("pubDate").text());
            Elements links = doc.getElementsByTag("item");  //A collection of articles from the parsed URL

            //Searches through each individual article
            for(Element link:links)
            {
                String id = UUID.randomUUID().toString();   //Assigns a random ID number for article
                NewsWebsite ign = new NewsWebsite(id,"IGN",null,siteURL,url,
                        buildDate,buildDate);
                Article curr = createArticle(link,ign);

                //Adds new Article to list if not already present in list
                if(!checkDuplicateArticles(curr)) {
                    articles.add(curr);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Article createArticle(Element e, NewsWebsite newsSite) throws ParseException {

        String title = e.select("title").text();
        String source = e.select("link").text();
        String publicationDate = e.select("pubDate").text();
        Date pubDate = format.parse(publicationDate);

        //TODO
        //Get Last Published Date
        String lastUpdated=e.select("").text();
        Date lastPubDate = pubDate;

        //Gets a short description of the article for viewing
        String snippet = e.select("description").text();
        if (snippet.length() > 255)
            snippet = snippet.substring(0,255);

        int impact = 0;

        //TODO
        //Calculate impact score
        //
        //Set article ID
        //
        //Capture article Image
        //
        //Get Impact Score


        Article curr= new Article(newsSite.getId(), title, source, newsSite, null,
                snippet, pubDate, lastPubDate, impact);
        return curr;
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
        Boolean dupe = false;

        //TODO
        //Check for duplicate articles

        return dupe;
    }
}
