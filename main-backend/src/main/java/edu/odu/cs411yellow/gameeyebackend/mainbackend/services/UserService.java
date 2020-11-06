package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Settings;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.UserStatus;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings.NotificationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides services for managing user profiles.
 */
@Service
public class UserService {

    private UserRepository users;

    @Autowired
    UserService(UserRepository users) {
        this.users = users;
    }

    /**
     * Gets the user profile with a specific firebase id.
     *
     * @param userId Id of the user profile to get.
     * @return User profile
     */
    public User getUser(final String userId) {
        return users.findUserById(userId);
    }

    /**
     * Checks if a user profile with a specific firebase id exists.
     *
     * @param userId Id of the user profile to check.
     * @return True if a user profile exists, false otherwise
     */
    public boolean checkUserExists(final String userId) {
        return users.existsById(userId);
    }

    /**
     * Creates a user profile with a specific firebase id.
     *
     * @param userId Id of the new user profile
     */
    public void createUser(final String userId) {
        User newUser = new User();
        newUser.setId(userId);
        newUser.setStatus(UserStatus.active);

        users.save(newUser);
    }

    /**
     * Deletes an existing user profile with a specific firebase id.
     *
     * @param userId Id of the user profile to delete
     */
    public void deleteUser(final String userId) {
        users.deleteById(userId);
    }

    /**
     * Sets a status to the user profile with a specific firebase id.
     *
     * @param userId     Id of the user profile to disable.
     * @param userStatus User profile status to set
     */
    public void setUserStatus(final String userId, final UserStatus userStatus) {
        User user = users.findUserById(userId);
        user.setStatus(userStatus);

        users.save(user);
    }

    /**
     * Removes articles for a watched game from a user's notifications.
     *
     * @param userId     Id of the user notifications to modify.
     * @param gameId     Id of the watched game.
     * @param articleIds Ids of articles to remove.
     */
    public void removeUserArticleNotifications(final String userId, String gameId, List<String> articleIds) {
        User user = users.findUserById(userId);

        List<WatchedGame> watchedGames = user.getWatchList();
        for (WatchedGame game: watchedGames) {
            if (game.getGameId().equals(gameId)) {
                game.removeArticlesFromResources(articleIds);
                break;
            }
        }

        users.save(user);
    }

    /**
     * Adds articles for a watched game from a user's notifications.
     *
     * @param userId     Id of the user notifications to modify.
     * @param gameId     Id of the watched game.
     * @param articleIds Ids of articles to add.
     */
    public void addUserArticleNotifications(final String userId, String gameId, List<String> articleIds) {
        User user = users.findUserById(userId);

        List<WatchedGame> watchedGames = user.getWatchList();
        for (WatchedGame game: watchedGames) {
            if (game.getGameId().equals(gameId)) {
                game.addArticlesToResources(articleIds);
                break;
            }
        }

        users.save(user);
    }

    public void updateSettings(final String firebaseID, Settings settings)
    {
        final User user = this.users.findUserById(firebaseID);
        user.setSettings(settings);
        users.save(user);
    }
}
