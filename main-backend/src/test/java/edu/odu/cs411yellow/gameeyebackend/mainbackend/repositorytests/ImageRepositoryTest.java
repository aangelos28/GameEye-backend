package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ImageRepository;
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
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Test
    public void saveAndDelete() {
        // Create a image
        Image image = new Image("1234", "logo", null);

        // Save image in database
        imageRepository.save(image);
        assert(imageRepository.existsById("1234"));

        // Delete image in database
        imageRepository.delete(image);
        assert(!imageRepository.existsById("1234"));
    }
}