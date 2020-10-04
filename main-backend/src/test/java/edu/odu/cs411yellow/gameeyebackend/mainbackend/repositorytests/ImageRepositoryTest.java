package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositorytests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ImageRepository;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    Image insertedImage;

    @BeforeEach
    public void insertImageIntoGameEyeTest() {

        String imageId = "5ea108ea34019c1d1c818c02";
        String imageType = "thumbnail";
        Binary imageData = new Binary(new byte[1]);

        Image testImage = new Image(imageId, imageType, imageData);

        insertedImage = testImage;

        imageRepository.save(insertedImage);
    }

    @AfterEach
    public void deleteImageFromGameEyeTest() {

        String imageId = insertedImage.getId();

        if(imageRepository.existsById(imageId))
            imageRepository.delete(insertedImage);

    }

    @Test
    public void findImageById() {
        String imageId = insertedImage.getId();

        Image foundImage = imageRepository.findImageById(imageId);

        assert(foundImage.getId().equals(insertedImage.getId()));
        assert(foundImage.getType().equals(insertedImage.getType()));
        assert(foundImage.getData().equals(insertedImage.getData()));
    }

}
