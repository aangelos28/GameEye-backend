package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticGameRepositoryCustom {
    SearchHits<ElasticGame> autocompleteGameTitle(final String title, final int maxResults);
    List<String> referencedGames(Article article);
    int commonStringSize(char [] X, char [] Y, int m, int n);

}
