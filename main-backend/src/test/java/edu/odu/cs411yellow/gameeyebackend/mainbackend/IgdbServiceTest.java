package edu.odu.cs411yellow.gameeyebackend.mainbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.GameResponse;
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
    public void testGetGameById () throws JsonProcessingException {

        int id = 1000;
        GameResponse gameResponse = igdbService.getGameResponseById(id);

        System.out.println(gameResponse.id + "\n");
        System.out.println(gameResponse.name + "\n");
        System.out.println(gameResponse.updated_at + "\n");
        System.out.println(gameResponse.genres.get(0) + "\n");
        System.out.println(gameResponse.websites.get(0) + "\n");


    }
}
