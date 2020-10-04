package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsWebsiteRepository extends MongoRepository<NewsWebsite, String> {
    NewsWebsite findByName(String name);

    boolean existsByName(String name);

    NewsWebsite findNewsWebsiteById(String id);

    boolean existsNewsWebsiteById(String id);
}
