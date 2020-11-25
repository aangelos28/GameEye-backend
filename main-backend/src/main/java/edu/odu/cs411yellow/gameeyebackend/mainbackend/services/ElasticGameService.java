package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void updateTitleLogoReleaseDate(ElasticGame updatedGame) {
        ElasticGame existingGame = elasticGames.findByGameId(updatedGame.getGameId());
        elasticGames.deleteByGameId(updatedGame.getGameId());

        existingGame.setTitle(updatedGame.getTitle());
        existingGame.setLogoUrl(updatedGame.getLogoUrl());
        existingGame.setReleaseDate(updatedGame.getReleaseDate());

        elasticGames.save(updatedGame);
    }

    public int bulkUpdateElastic(List<ElasticGame> updatedGames) {
        List<ElasticGame> toBeInserted = new ArrayList<>();
        List<ElasticGame> toBeDeleted = new ArrayList<>();

        for (ElasticGame game: updatedGames) {
            try {
                ElasticGame existingGame = elasticGames.findByGameId(game.getGameId());
                toBeDeleted.add(existingGame);

                // Update elastic game title, logoUrl, and releaseDate
                existingGame.setTitle(game.getTitle());
                existingGame.setLogoUrl(game.getLogoUrl());
                existingGame.setReleaseDate(game.getReleaseDate());

                toBeInserted.add(existingGame);
            } catch (Exception e) {
                System.out.println(String.format("Unable to update ES game. Title: %1$s. Mongo Id: %2$s.",
                                   game.getTitle(), game.getGameId()));
                e.printStackTrace();
            }
        }

        elasticGames.deleteAll(toBeDeleted);
        elasticGames.saveAll(toBeInserted);

        return toBeInserted.size();
    }

    public void save(ElasticGame game) {
        elasticGames.save(game);
    }
}
