package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryCustom {
    public void removeUserArticleNotifications(final String userId, final String gameId, List<String> articleIds);
}
