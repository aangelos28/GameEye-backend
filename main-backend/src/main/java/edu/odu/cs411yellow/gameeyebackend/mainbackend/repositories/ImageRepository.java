package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    boolean existsById(String id);

    Image findImageById(String id);

    void deleteById(String id);

}