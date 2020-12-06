package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class ReferenceGameServiceTest {

    @Autowired
    ReferenceGameService referenceService;

    @Autowired
    ElasticGameRepository elasticGames;

    @Test
    public void testGetReferencedGame() {
        final String gameTitle1 = "The Legend of Zelda: Oracle of Ages";
        final String articleTitle1 = "Nintendo announces remake of The Legend of Zelda: Oracle of Ages and Seasons";

        final String gameTitle2 = "Metal Gear Solid 3: Snake Eater";
        final String articleTitle2 = "New developments: Metal Gear Solid 3: Snake Eater is getting DLC content completing the game.";

        final String gameTitle3 = "Cyberpunk 2077";
        final String articleTitle3 = "For the third time Cyberpunk 2077 is delayed!";

        final String gameTitle4 = "Persona 4 Golden";
        final String articleTitle4 = "With announcement of PS5 Atlas games announces P4 Golden remake of Persona 4 Golden";

        Article article1 = new Article();
        article1.setTitle(articleTitle1);

        Article article2 = new Article();
        article2.setTitle(articleTitle2);

        Article article3 = new Article();
        article3.setTitle(articleTitle3);

        Article article4 = new Article();
        article4.setTitle(articleTitle4);

        // Confirm that gameTitle1 is returned among the reference results
        boolean isReturned1 = false;

        List<String> gameIds1 = referenceService.getReferencedGames(article1);
        for (String gameId: gameIds1) {
            if (elasticGames.findByGameId(gameId).getTitle().equals(gameTitle1)) {
                isReturned1 = true;
                break;
            }
        }

        assertThat(isReturned1, is(true));

        // Confirm that gameTitle2 is returned among the reference results
        boolean isReturned2 = false;

        List<String> gameIds2 = referenceService.getReferencedGames(article2);
        for (String gameId: gameIds2) {
            if (elasticGames.findByGameId(gameId).getTitle().equals(gameTitle2)) {
                isReturned2 = true;
                break;
            }
        }

        assertThat(isReturned2, is(true));

        // Confirm that gameTitle3 is returned among the reference results
        boolean isReturned3 = false;

        List<String> gameIds3 = referenceService.getReferencedGames(article3);
        for (String gameId: gameIds3) {
            if (elasticGames.findByGameId(gameId).getTitle().equals(gameTitle3)) {
                isReturned3 = true;
                break;
            }
        }

        assertThat(isReturned3, is(true));

        // Confirm that gameTitle4 is returned among the reference results
        boolean isReturned4 = false;

        List<String> gameIds4 = referenceService.getReferencedGames(article4);
        for (String gameId: gameIds4) {
            if (elasticGames.findByGameId(gameId).getTitle().equals(gameTitle4)) {
                isReturned4 = true;
                break;
            }
        }

        assertThat(isReturned4, is(true));
    }
}
