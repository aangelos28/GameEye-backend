package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.NewsWebsite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface NewsWebsiteRepository extends MongoRepository<NewsWebsite, String> {
    // add, delete, edit, find
    List<NewsWebsite> findAll();

    @Query("{}")
    Stream<NewsWebsite> findAllByCustomQueryWithStream();
}
