package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticGameService {
    private ElasticGameRepository elasticGames;

    @Autowired
    public ElasticGameService(ElasticGameRepository elasticGames) {
        this.elasticGames = elasticGames;
    }

    public void saveAll(List<ElasticGame> games) {
        elasticGames.saveAll(games);
    }

    public boolean existsByTitle(String title) {
        return elasticGames.existsByTitle(title);
    }

    public void updateTitle(ElasticGame game) {
        elasticGames.deleteById(game.getGameId());
        elasticGames.save(game);
    }

    public void save(ElasticGame game) {
        elasticGames.save(game);
    }
}
