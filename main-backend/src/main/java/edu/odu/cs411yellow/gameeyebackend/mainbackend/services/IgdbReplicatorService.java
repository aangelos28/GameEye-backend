package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IgdbReplicatorService {
    GameRepository games;

    Logger logger = LoggerFactory.getLogger(IgdbReplicatorService.class);

    @Autowired
    public IgdbReplicatorService(GameRepository games) {
        this.games = games;
    }
}
