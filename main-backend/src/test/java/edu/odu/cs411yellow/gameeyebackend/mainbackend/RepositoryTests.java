package edu.odu.cs411yellow.gameeyebackend.mainbackend;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
public class RepositoryTests {
    @Test
    public void testMongoConnection() {
    }
}
