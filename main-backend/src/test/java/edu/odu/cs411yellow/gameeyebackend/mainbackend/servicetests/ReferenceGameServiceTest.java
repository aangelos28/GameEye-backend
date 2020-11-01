package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class ReferenceGameServiceTest {

    @Autowired
    ReferenceGameService referenceGameService;

    @Autowired
    ElasticGameRepository gameRepository;

    public void testGetReferencedGame() {
        final String gameTitle = "Fallout 3";
        final String articleTitle = "There has been a resurgence in Fallout 3 " +
                                    "mods among the gaming community";

        Article article = new Article();
        article.setTitle(articleTitle);

        List<String> gameIDs= referenceGameService.getReferencedGames(article);
        String gameID = gameIDs.get(0);
        ElasticGame game =  gameRepository.findByGameId(gameID);

        assert(gameTitle.contentEquals(game.getTitle()));

    }

}
