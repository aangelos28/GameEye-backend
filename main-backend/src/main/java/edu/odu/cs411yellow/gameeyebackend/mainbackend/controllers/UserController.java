package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * REST API for interacting with user profiles.
 */
@RestController
public class UserController {
    private final UserService userService;

    private final GameService gameService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    /**
     * Request body for admin endpoints.
     */
    private static class UserRequest {
        public String userId;
    }

    /**
     * Checks if a user profile exists.
     * @return True if the user profile exists, false otherwise
     */
    @GetMapping(path = "/private/user/exists")
    public boolean checkUserExists() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        // Ensure that the user profile does not already exist
        return userService.checkUserExists(userId);
    }

    /**
     * Creates a user profile if it does not already exist.
     */
    @PostMapping(path = "/private/user/create")
    public ResponseEntity<String> createUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        // Ensure that the user profile does not already exist
        if (userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Create the user profile
        userService.createUser(userId);
        logger.info(String.format("Created user profile with firebase id %s", userId));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a user profile if it does not already exist.
     */
    @DeleteMapping(path = "/private/user/delete")
    public ResponseEntity<String> deleteUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        // Ensure that the user profile already exists
        if (!userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Delete the user profile
        userService.deleteUser(userId);
        logger.info(String.format("Deleted user profile with firebase id %s", userId));

        return ResponseEntity.ok("User profile deleted.");
    }

    /**
     * Creates a user profile if it does not already exist.
     */
    @PostMapping(path = "/private-admin/user/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUserAdmin(@RequestBody UserRequest request) {
        final String userId = request.userId;

        // Ensure that the user profile does not already exist
        if (userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Create the user profile
        userService.createUser(userId);
        logger.info(String.format("ADMIN: Created user profile with firebase id %s", userId));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Deletes a user profile if it does not already exist.
     */
    @DeleteMapping(path = "/private-admin/user/delete")
    public ResponseEntity<String> deleteUserAdmin(@RequestBody UserRequest request) {
        final String userId = request.userId;

        // Ensure that the user profile already exists
        if (!userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Delete the user profile
        userService.deleteUser(userId);
        logger.info(String.format("ADMIN: Deleted user profile with firebase id %s", userId));

        return ResponseEntity.ok("User profile deleted.");
    }

    public static class ArticleNotificationsRequest {
        public String gameId;
        public List<String> articleIds;

        public ArticleNotificationsRequest(String gameId, List<String> articleIds) {
            this.gameId = gameId;
            this.articleIds = articleIds;
        }
    }

    /**
     * Removes articles for a watched game from a user's notifications.
     */
    @PutMapping(path = "/private/user/notifications/articles/remove/")
    public ResponseEntity<?> removeUserArticleNotifications(@RequestBody ArticleNotificationsRequest request) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String userId = fbToken.getUid();

        if (!userService.checkUserExists(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!gameService.existsById(request.gameId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        userService.removeUserArticleNotifications(userId, request.gameId, request.articleIds);

        return ResponseEntity.ok("Article notifications removed.");
    }
}
