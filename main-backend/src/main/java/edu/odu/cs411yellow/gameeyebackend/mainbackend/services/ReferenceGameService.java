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
        int articleTitleLength = articleTitle.length();
        int longestMatchSize = 0;
        List <String> matchingIDs = new ArrayList<>();

        for (var i: referencedGames) {

            String gameTitle = i.getContent().getTitle();
            int gameTitleLength = gameTitle.length();
            int matchSize;

            matchSize = commonStringSize(articleTitle.toCharArray(),gameTitle.toCharArray(),
                    articleTitleLength,gameTitleLength);

            //Assuming gameTitles are no less than 5 characters
            if((longestMatchSize < matchSize) && (matchSize > 5)) {
                longestMatchSize = matchSize;   //set new longest match
                matchingIDs.clear();
                matchingIDs.add(i.getContent().getGameId());
            }

            else if (longestMatchSize == matchSize){
                matchingIDs.add(i.getContent().getGameId());
            }

        }

        return matchingIDs;
    }

    public int commonStringSize(char [] X, char [] Y, int m, int n) {

        int [][] longCommStr = new int[m + 1][n + 1];
        int result = 0;  //Store length of the longest common substring

        //Loop through both char arrays to find longest common string
        for (int i = 0; i <= m; i++)
        {
            for (int j = 0; j <= n; j++)
            {
                if (i == 0 || j == 0)
                    longCommStr[i][j] = 0;
                else if (X[i - 1] == Y[j - 1])
                {
                    longCommStr[i][j] = longCommStr[i - 1][j - 1] + 1;
                    result = Integer.max(result, longCommStr[i][j]);
                }
                else
                    longCommStr[i][j] = 0;
            }
        }
        return result;
    }
}
