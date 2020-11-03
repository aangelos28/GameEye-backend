package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.junit.Test;
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

    @Test
    public void testGetReferencedGame() {
        final String gameTitle1 = "Fallout 3";
        final String articleTitle1 = "There has been a resurgence in Fallout 3 " +
                                    "mods among the gaming community";

        final String gameTitle2 = "Metal Gear Solid V: The Phantom Pain";
        final String articleTitle2 = "New developments: MGS The Phantom Pain" +
                                    "is getting DLC content completing the game.";

        final String gameTitle3 = "Call of Duty: Black Ops 4";
        final String articleTitle3 = "COD: Black Ops 4 is more like" +
                                    "Black Ops III blah blah new DLC" +
                                    "Black Ops II Not as good as Black Ops";

        final String gameTitle4 = "Grand Theft Auto V";
        final String articleTitle4 = "Rockstar releasing more DLC in GTA V " +
                                    "blah blah Expensive MicroTransactions";

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
        ElasticGame game1 =  gameRepository.findByGameId(gameID1);

        List<String> gameIDs2 = referenceGameService.getReferencedGames(article2);
        String gameID2 = gameIDs2.get(0);
        ElasticGame game2 =  gameRepository.findByGameId(gameID2);

        List<String> gameIDs3 = referenceGameService.getReferencedGames(article3);
        String gameID3 = gameIDs3.get(0);
        ElasticGame game3 =  gameRepository.findByGameId(gameID3);

        List<String> gameIDs4 = referenceGameService.getReferencedGames(article4);
        String gameID4 = gameIDs4.get(0);
        ElasticGame game4 =  gameRepository.findByGameId(gameID4);

        assert(gameTitle1.contentEquals(game1.getTitle()));
        assert(gameTitle2.contentEquals(game2.getTitle()));
        assert(gameTitle3.contentEquals(game3.getTitle()));
        assert(gameTitle4.contentEquals(game4.getTitle()));

    }

}
