package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsWebsiteRepository extends MongoRepository<NewsWebsite, String> {
    NewsWebsite findByName(String name);

    Boolean existsByName(String name);
}