package edu.odu.cs411yellow.gameeyebackend.mainbackend;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.IgdbModel;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class IgdbServiceTest {

    WebClient.Builder webClientBuilder;

    String igdbUrl = "";
    String igdbKey = "";

    @Autowired
    IgdbService igdbService;

    @BeforeEach
    public void setup () {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetCompanies () {

        String igdbOutput = igdbService.getCompanies();

    }

    @Test
    public void testGetGameById () {

        int id = 1000;
        IgdbModel.GameResponse gameResponse = igdbService.getGameResponseById(id);

        System.out.println(gameResponse.toString());
    }
}
