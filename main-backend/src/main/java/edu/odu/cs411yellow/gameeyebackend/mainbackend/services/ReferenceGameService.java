package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReferenceGameService {

    ElasticGameRepository elasticGames;

    @Autowired
    public ReferenceGameService(ElasticGameRepository elasticGames){
        this.elasticGames = elasticGames;
    }

    /**
     * Finds referenced games for a given news article
     *
     * @param article News Article to get article title from
     * @return List of IDs of referenced games.
     */
    public List<String> getReferencedGames(Article article) {

        String articleTitle = article.getTitle();
        SearchHits<ElasticGame> referencedGames = elasticGames.autocompleteGameTitle(articleTitle, 25);
        List<String> matchingIDs = new ArrayList<>();

        for (var game : referencedGames) {

            String gameTitle = game.getContent().getTitle();
            int gameTitleLength = gameTitle.length();

            //Case exact match is found
            if (articleTitle.contains(gameTitle)) {
                String exactMatch = game.getContent().getGameId();

                // Add exact match if non-empty string and not already in matchingIDs array
                if (!exactMatch.contentEquals("") && !matchingIDs.contains(exactMatch)) {
                    matchingIDs.add(0, exactMatch);
                }
            }
        }

        return matchingIDs;
    }
}
