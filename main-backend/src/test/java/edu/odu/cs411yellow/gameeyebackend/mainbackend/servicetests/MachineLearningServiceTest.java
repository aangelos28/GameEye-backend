package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.MachineLearningService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class MachineLearningServiceTest {
    @Autowired
    MachineLearningService mlService;

    @Test
    public void testPredictArticleImportance() {
        final List<String> articleTitles = new ArrayList<>();
        articleTitles.add("New Release Date for Assasin's Creed Valhalla");
        articleTitles.add("54 Things Breath of the Wild Fans Will Love About Age of Calamity");
        articleTitles.add("Gears of War Mobile Spin-Off Gears Pop! To Shut Down in 2021");

        final List<Boolean> impactScores = mlService.predictArticleImportance(articleTitles);
        assertThat(impactScores, contains(true, false, true));
    }
}
