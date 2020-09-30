package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GameSpotWebScraper implements WebScraper {

    private static String rssFeed = "https://www.gamespot.com/feeds/game-news/";
    private List<Article> articles;
    private DateFormat format = new SimpleDateFormat("E, d MMMM yyyy kk:mm:ss z");

    /**
     * Default Constructor
     */
//    public GameSpotWebScraper() {
//        articles = new List<Article>() {
//            @Override
//            public int size() {
//                return 0;
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public boolean contains(Object o) {
//                return false;
//            }
//
//            @Override
//            public Iterator<Article> iterator() {
//                return null;
//            }
//
//            @Override
//            public Object[] toArray() {
//                return new Object[0];
//            }
//
//            @Override
//            public <T> T[] toArray(T[] a) {
//                return null;
//            }
//
//            @Override
//            public boolean add(Article article) {
//                return false;
//            }
//
//            @Override
//            public boolean remove(Object o) {
//                return false;
//            }
//
//            @Override
//            public boolean containsAll(Collection<?> c) {
//                return false;
//            }
//
//            @Override
//            public boolean addAll(Collection<? extends Article> c) {
//                return false;
//            }
//
//            @Override
//            public boolean addAll(int index, Collection<? extends Article> c) {
//                return false;
//            }
//
//            @Override
//            public boolean removeAll(Collection<?> c) {
//                return false;
//            }
//
//            @Override
//            public boolean retainAll(Collection<?> c) {
//                return false;
//            }
//
//            @Override
//            public void clear() {
//
//            }
//
//            @Override
//            public Article get(int index) {
//                return null;
//            }
//
//            @Override
//            public Article set(int index, Article element) {
//                return null;
//            }
//
//            @Override
//            public void add(int index, Article element) {
//
//            }
//
//            @Override
//            public Article remove(int index) {
//                return null;
//            }
//
//            @Override
//            public int indexOf(Object o) {
//                return 0;
//            }
//
//            @Override
//            public int lastIndexOf(Object o) {
//                return 0;
//            }
//
//            @Override
//            public ListIterator<Article> listIterator() {
//                return null;
//            }
//
//            @Override
//            public ListIterator<Article> listIterator(int index) {
//                return null;
//            }
//
//            @Override
//            public List<Article> subList(int fromIndex, int toIndex) {
//                return null;
//            }
//        };
//    }
    /**
     *  Constructor
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
            Document feed = Jsoup.connect(rssFeed).get();

            Date buildDate = format.parse(feed.selectFirst("lastBuildDate").text());
            NewsWebsite GameSpot = new NewsWebsite(UUID.randomUUID().toString(),"GameSpot", null,
                    "https://www.gamespot.com/", rssFeed, buildDate,buildDate);

            Elements items = feed.select("item");

            for (var i : items){

                //TODO Prevent Duplicate articles
                Article toAdd = createArticle(i,GameSpot);

                //Puts it in JSON format
                Gson json = new GsonBuilder().setPrettyPrinting().create();
                System.out.print(json.toJson(toAdd));

//                boolean duplicateExists = false;

//                for (Article a : articles) {
//                    if (toAdd.getTitle().contentEquals(a.getTitle()))
//                        duplicateExists = true;
//                }
//                if (!duplicateExists)
                    articles.add(toAdd);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

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
        if (snippet.length() > 255)
            snippet = snippet.substring(0,255);

        //Create a Unique ID
        String id = UUID.randomUUID().toString();
        return new Article(id, title, url, site,
                new Image(id, ".jpg",null), snippet, publicationDate, publicationDate, 0);

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
