package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Provides services for managing user profiles.
 */
@Service
public class UserService {

    UserRepository users;

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
        return users.findUserByFirebaseId(userId);
    }

    /**
     * Checks if a user profile with a specific firebase id exists.
     *
     * @param userId Id of the user profile to check.
     * @return True if a user profile exists, false otherwise
     */
    public boolean checkUserExists(final String userId) {
        return users.existsByFirebaseId(userId);
    }

    /**
     * Creates a user profile with a specific firebase id.
     *
     * @param userId Id of the new user profile
     */
    public void createUser(final String userId) {
        User newUser = new User();
        newUser.setFirebaseId(userId);
        newUser.setStatus(UserStatus.active);

        users.save(newUser);
    }

    /**
     * Deletes an existing user profile with a specific firebase id.
     *
     * @param userId Id of the user profile to delete
     */
    public void deleteUser(final String userId) {
        users.deleteByFirebaseId(userId);
    }

    /**
     * Sets a status to the user profile with a specific firebase id.
     *
     * @param userId     Id of the user profile to disable.
     * @param userStatus User profile status to set
     */
    public void setUserStatus(final String userId, UserStatus userStatus) {
        User user = users.findUserByFirebaseId(userId);
        user.setStatus(userStatus);

        users.save(user);
    }
}