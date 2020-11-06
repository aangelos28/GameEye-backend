package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
//@TestPropertySource(locations="classpath:application-test.properties")
public class ReferenceGameServiceTest {

    @Autowired
    ReferenceGameService referenceGameService;

    @Autowired
    ElasticGameRepository gameRepository;

    @Test
    public void testGetReferencedGame() {
        final String gameTitle1 = "The Legend of Zelda: Oracle of Ages";
        final String articleTitle1 = "Nintendo announces remake of Zelda Oracle of Ages and Seasons";

        final String gameTitle2 = "Metal Gear Solid 3: Snake Eater";
        final String articleTitle2 = "New developments: MGS Snake Eater" +
                                    "is getting DLC content completing the game.";

        final String gameTitle3 = "Cyberpunk 2077";
        final String articleTitle3 = "For the third time Cyberpunk 2077 is delayed!";

        final String gameTitle4 = "Persona 4 Golden";
        final String articleTitle4 = "With anouncement of PS5 Atlas games" +
                                    "anouncences P4 Golden remake";


        Article article1 = new Article();
        article1.setTitle(articleTitle1);

        Article article2 = new Article();
        article2.setTitle(articleTitle2);

        Article article3 = new Article();
        article3.setTitle(articleTitle3);

        Article article4 = new Article();
        article4.setTitle(articleTitle4);


        List<String> gameIDs1 = referenceGameService.getReferencedGames(article1);
        String gameID1 = gameIDs1.get(0);
        ElasticGame elasticGame1 =  gameRepository.findByGameId(gameID1);

        List<String> gameIDs2 = referenceGameService.getReferencedGames(article2);
        String gameID2 = gameIDs2.get(0);
        ElasticGame elasticGame2 =  gameRepository.findByGameId(gameID2);

        List<String> gameIDs3 = referenceGameService.getReferencedGames(article3);
        String gameID3 = gameIDs3.get(0);
        ElasticGame elasticGame3 =  gameRepository.findByGameId(gameID3);

        List<String> gameIDs4 = referenceGameService.getReferencedGames(article4);
        String gameID4 = gameIDs4.get(0);
        ElasticGame elasticGame4 =  gameRepository.findByGameId(gameID4);


        assert(gameTitle1.contentEquals(elasticGame1.getTitle()));
        assert(gameTitle2.contentEquals(elasticGame2.getTitle()));
        assert(gameTitle3.contentEquals(elasticGame3.getTitle()));
        assert(gameTitle4.contentEquals(elasticGame4.getTitle()));

    }

}
