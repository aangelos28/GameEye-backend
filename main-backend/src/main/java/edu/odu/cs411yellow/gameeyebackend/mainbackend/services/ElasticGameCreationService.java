package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ElasticGameCreationService {
    ElasticGameRepository elasticGames;
    MongoOperations mongo;

    Logger logger = LoggerFactory.getLogger(ElasticGameCreationService.class);

    @Autowired
    ElasticGameCreationService(ElasticGameRepository elasticGames, MongoOperations mongo) {
        this.elasticGames = elasticGames;
        this.mongo = mongo;
    }

    /**
     * Inserts the title and id of every game to ElasticSearch.
     * Note: The ElasticSearch index should be empty.
     *
     * @return Number of created elastic games
     */
    public long createElasticGamesFromGames() {
        final MongoCollection<Document> gamesCollection = mongo.getCollection(mongo.getCollectionName(Game.class));

        final FindIterable<Document> iterable = gamesCollection.find();

        final int bufferSize = 10000;
        List<ElasticGame> elasticGameBuffer = new ArrayList<>(bufferSize);

        // Iterate over all games and add them to ElasticSearch
        logger.info("Inserting games to ElasticSearch");
        long i = 1;
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ElasticGame elasticGame = new ElasticGame();
                elasticGame.setGameId(doc.get("_id").toString());
                elasticGame.setTitle((String) doc.get("title"));

                elasticGameBuffer.add(elasticGame);

                // Bulk insert every bufferSize documents
                if (i % bufferSize == 0) {
                    elasticGames.saveAll(elasticGameBuffer);
                    elasticGameBuffer.clear();
                }

                ++i;
            }
        }

        // Save remaining games
        elasticGames.saveAll(elasticGameBuffer);
        elasticGameBuffer.clear();

        logger.info(String.format("ADMIN: Inserted %d games to ElasticSearch.", i - 1));

        return i - 1;
    }
}
