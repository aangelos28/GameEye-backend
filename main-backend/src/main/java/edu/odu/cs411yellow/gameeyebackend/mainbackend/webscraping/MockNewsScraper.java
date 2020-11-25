package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscraping;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MockNewsScraper implements WebScraper {

    NewsWebsiteRepository newsWebsites;

    final private String name = "GameEye Mock News";

    @Autowired
    public MockNewsScraper(NewsWebsiteRepository newsWebsites) {
        this.newsWebsites = newsWebsites;
    }

    /**
     * Initiate the scrape
     */
    public List<Article> scrape(String name) {
        List<Article> articles = new ArrayList<>();
        String url = newsWebsites.findByName(name).getSiteUrl();

        try {
            NewsWebsite mockNews = newsWebsites.findByName(name);

            Document RssFeed = Jsoup.parse(Jsoup.connect(url).get().select("ul").toString());

            Elements items = RssFeed.select("div");
            for (var i : items) {
                Article toAdd = createArticle(i, mockNews.getName());
                articles.add(toAdd);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return articles;
    }

    @Override
    public Article createArticle(Element i, String websiteName) throws ParseException {
        DateFormat format = new SimpleDateFormat("E, MMMM d, yyyy");

        //Get Info
        String title = i.select("h2").text();

        //Parse Link
        String url = i.select("a").toString();
        String delims = "\"";
        String[] parse = url.split(delims);
        url = "https://gameeye-mock-news.netlify.app" + parse[1];

        String pubDate = i.selectFirst("p").text();
        Date date = format.parse(pubDate);

        String snippet = i.selectFirst("p").nextElementSibling().text();
        if (snippet.length() > 255) {
            snippet = snippet.substring(0, 255);
        }

        Article article = new Article();
        article.setTitle(title);
        article.setUrl(url);
        article.setNewsWebsiteName(websiteName);
        article.setSnippet(snippet);
        article.setPublicationDate(date);
        article.setLastUpdated(date);
        article.setIsImportant(false);

        return article;
    }

    /**
     * Retrieves name of the scraper
     *
     * @return String
     */
    public String getScraperName() {
        return name;
    }
}
