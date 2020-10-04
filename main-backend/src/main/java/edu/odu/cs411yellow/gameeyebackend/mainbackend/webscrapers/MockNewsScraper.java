package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("MockNewsScrape")
public class MockNewsScraper implements WebScraper{

    private static final String newsSite = "https://gameeye-mock-news.netlify.app/";
    private List<Article> articles;
    public static DateFormat format = new SimpleDateFormat("E, MMMM d, yyyy");

//  NOTE: GameEye Mock News is not in the database yet.
//    @Autowired
//    private NewsWebsiteRepository siteBuilder;

    /**
     * Constructor
     * @param articles from feed
     */
    public MockNewsScraper(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Initiate the scrape
     */
    @Override
    public void scrape() {

        try {
            Document feed = Jsoup.parse(Jsoup.connect(newsSite).get().select("ul").toString());

//            NewsWebsite mockNews = siteBuilder.findByName("GameEye Mock News");
            NewsWebsite mockNews = new NewsWebsite(UUID.randomUUID().toString(), "GameEyre Mock News",
                    null, newsSite, newsSite, null);

            Elements items = feed.select("div");
            for (var i : items){

                Article toAdd = createArticle(i,mockNews);

                if (!checkDuplicateArticles(toAdd))
                    articles.add(toAdd);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public Article createArticle(Element i, NewsWebsite site) throws ParseException {


        //Get Info
        String title = i.select("h2").text();

        //Parse Link
        String url = i.select("a").toString();
        String delims = "\"";
        String [] parse = url.split(delims);
        url = newsSite + parse[1];

        String pubDate = i.selectFirst("p").text();
        Date date = format.parse(pubDate);

        String snippet = i.selectFirst("p").nextElementSibling().text();

        String id = UUID.randomUUID().toString();
        return new Article(id , title, url, site,
                null, snippet, date, date, 0);

    }

    @Override
    public Boolean checkDuplicateArticles(Article a) {

        for (Article i : articles) {
            if (a.getTitle().contentEquals(i.getTitle()))
                return true;
        }

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
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        return json.toJson(this.articles);
    }


}
