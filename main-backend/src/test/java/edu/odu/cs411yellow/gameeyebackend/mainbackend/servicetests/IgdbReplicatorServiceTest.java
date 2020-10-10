package edu.odu.cs411yellow.gameeyebackend.mainbackend.servicetests;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbReplicatorService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class IgdbReplicatorServiceTest {
    @Autowired
    IgdbReplicatorService igdbReplicator;

    @Autowired
    GameRepository gameRepository;

    @Test
    public void testReplicateIgdbByRange() throws JsonProcessingException {
        int minId = 1;
        int maxId = 10;

        igdbReplicator.replicateIgdbByRange(minId, maxId);

    }
}