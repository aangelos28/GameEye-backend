package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import java.util.List;


public interface WebScraperInterface {

    //Initiates web scraping
    void beginScrape();

    //Retrieves a list of the extracted news articles
    List<Article> retrieveList();

    //Retrieves a specific news article provided an index
    Article retrieveArticle(int index);

}
