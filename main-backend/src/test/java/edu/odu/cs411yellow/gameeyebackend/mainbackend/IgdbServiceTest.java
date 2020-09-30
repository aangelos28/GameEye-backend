package edu.odu.cs411yellow.gameeyebackend.mainbackend;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IgdbServiceTest {

    @BeforeEach
    public void setup () {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetCompanies () {
        IgdbService igdbService;

        String igdbOutput = igdbService.getCompanies();
    }
}
