package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import com.google.firebase.messaging.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Settings;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.settings.NotificationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling notification functionality.
 */
@Service
public class NotificationService {
    private final UserService userService;
    private final GameService gameService;

    @Autowired
    NotificationService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public enum SubscriptionOperation {
        SUBSCRIBE, UNSUBSCRIBE
    }

    /**
     * Sends a notification to a specific user.
     *
     * @param userId   IDs of the user to receive notification
     * @param title    Notification title
     * @param body     Notification body text
     * @param imageUrl Notification image url
     * @throws FirebaseMessagingException Thrown if the notification could not be sent
     */
    public void sendTargetedNotification(final String userId, final String title, final String body, final String imageUrl) throws FirebaseMessagingException {
        final User user = userService.getUser(userId);
        final List<String> notificationTokens = user.getFcmTokens();

        System.out.println(notificationTokens);

        final MulticastMessage message = MulticastMessage.builder().setNotification(
                Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .setImage(imageUrl)
                        .build())
                .addAllTokens(notificationTokens)
                .build();

        final BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();

            // Delete failed tokens from database
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    userService.deleteNotificationToken(userId, notificationTokens.get(i));
                }
            }
        }
    }

    /**
     * Send a notification for a specific topic.
     *
     * @param topic    Topic name
     * @param title    Notification title
     * @param body     Notification body text
     * @param imageUrl Notification image url
     * @throws FirebaseMessagingException Thrown if the notification could not be sent
     */
    public void sendTopicNotification(final String topic, final String title, final String body, final String imageUrl) throws FirebaseMessagingException {
        final Message message = Message.builder().setNotification(
                Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .setImage(imageUrl)
                        .build())
                .setTopic(topic)
                .build();

        FirebaseMessaging.getInstance().send(message);
    }

    /**
     * Modifies the notification subscription of a user for a specific game
     *
     * @param user         User object
     * @param gameId       ID of the game to modify notification subscription for
     * @param subOperation Subscription operation (SUBSCRIBE/UNSUBSCRIBE)
     * @throws Exception Thrown if the game does not exist or the user has not chosen to receive notifications
     */
    public void modifyUserGameSubscription(final String userId, final String gameId, SubscriptionOperation subOperation) throws Exception {
        final User user = userService.getUser(userId);
        final Settings settings = user.getSettings();
        final NotificationSettings notificationSettings = settings.getNotificationSettings();

        if (!notificationSettings.getReceiveNotifications()) {
            return;
        }
        if (!gameService.existsById(gameId)) {
            throw new Exception("Cannot subscribe user to game. Invalid game id.");
        }

        // Subscribe or unsubscribe user to game notifications
        final List<String> notificationTokens = user.getFcmTokens();

        TopicManagementResponse response;
        if (notificationSettings.getNotifyOnlyIfImportant()) {
            if (subOperation == SubscriptionOperation.SUBSCRIBE) {
                // Subscribe to important
                response = FirebaseMessaging.getInstance().subscribeToTopic(notificationTokens, String.format("%s_important", gameId));
                FirebaseMessaging.getInstance().unsubscribeFromTopic(notificationTokens, String.format("%s_regular", gameId));
            } else {
                // Unsubscribe from important
                response = FirebaseMessaging.getInstance().unsubscribeFromTopic(notificationTokens, String.format("%s_important", gameId));
                FirebaseMessaging.getInstance().subscribeToTopic(notificationTokens, String.format("%s_regular", gameId));
            }
        } else {
            if (subOperation == SubscriptionOperation.SUBSCRIBE) {
                // Subscribe to all
                response = FirebaseMessaging.getInstance().subscribeToTopic(notificationTokens, String.format("%s_regular", gameId));
                FirebaseMessaging.getInstance().subscribeToTopic(notificationTokens, String.format("%s_important", gameId));
            } else {
                // Umsubscribe from all
                response = FirebaseMessaging.getInstance().unsubscribeFromTopic(notificationTokens, String.format("%s_regular", gameId));
                FirebaseMessaging.getInstance().unsubscribeFromTopic(notificationTokens, String.format("%s_important", gameId));
            }
        }

        // Handle errors
        if (response.getFailureCount() > 0) {
            List<TopicManagementResponse.Error> errors = response.getErrors();

            // Delete unregistered tokens from database
            for (int i = 0; i < errors.size(); i++) {
                TopicManagementResponse.Error error = errors.get(i);
                if ((error.getReason().equals("UNREGISTERED"))) {
                    userService.deleteNotificationToken(user.getId(), notificationTokens.get(i));
                }
            }
        }
    }

    /**
     * Modified all the game subscriptions for a specific user.
     *
     * @param user          User objects
     * @param subOperations List of topics suffixes and the subscription operation for each
     */
    public void modifyAllUserGameSubscriptions(final User user, TopicSubscriptionOperation[] subOperations) {
        final List<String> notificationTokens = user.getFcmTokens();
        final List<WatchedGame> watchlist = user.getWatchList();

        for (WatchedGame watchedGame : watchlist) {
            for (TopicSubscriptionOperation subOperation : subOperations) {
                if (subOperation.getSubscriptionOperation() == SubscriptionOperation.SUBSCRIBE) {
                    FirebaseMessaging.getInstance().subscribeToTopicAsync(notificationTokens, String.format("%s_%s", watchedGame.getGameId(), subOperation.getTopicSuffix()));
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopicAsync(notificationTokens, String.format("%s_%s", watchedGame.getGameId(), subOperation.getTopicSuffix()));
                }
            }
        }
    }

    /**
     * Modify all user notification subscriptions depending on changed settings.
     *
     * @param user                    User object with new notification settings
     * @param oldNotificationSettings Previous notifications settings (before being changed)
     */
    public void modifyUserSubscriptions(final User user, NotificationSettings oldNotificationSettings) {
        final Settings settings = user.getSettings();
        final NotificationSettings newNotificationSettings = user.getSettings().getNotificationSettings();
        final boolean newReceiveNotifications = newNotificationSettings.getReceiveNotifications();
        final boolean oldReceiveNotifications = oldNotificationSettings.getReceiveNotifications();
        final boolean newNotifyOnlyIfImportant = newNotificationSettings.getReceiveNotifications();
        final boolean oldNotifyOnlyIfImportant = oldNotificationSettings.getNotifyOnlyIfImportant();

        final boolean receiveNotificationsChanged = newReceiveNotifications != oldReceiveNotifications;
        final boolean notifyOnlyIfImportantChanged = newNotifyOnlyIfImportant != oldNotifyOnlyIfImportant;

        // Check if receive notification setting has changed
        if (receiveNotificationsChanged) {
            // User has selected to not receive notifications
            if (!newReceiveNotifications) {
                modifyAllUserGameSubscriptions(user, new TopicSubscriptionOperation[]{
                        new TopicSubscriptionOperation("regular", SubscriptionOperation.UNSUBSCRIBE),
                        new TopicSubscriptionOperation("important", SubscriptionOperation.UNSUBSCRIBE)
                });
            }

            // Subscribe user to notifications (at least important notifications)
            modifyAllUserGameSubscriptions(user, new TopicSubscriptionOperation[]{
                    new TopicSubscriptionOperation("important", SubscriptionOperation.SUBSCRIBE)
            });

            if (!newNotifyOnlyIfImportant) {
                // Subscribe to all user notifications
                modifyAllUserGameSubscriptions(user, new TopicSubscriptionOperation[]{
                        new TopicSubscriptionOperation("regular", SubscriptionOperation.SUBSCRIBE)
                });
            }
        }

        // Check if notify only if important setting has changed
        if (newReceiveNotifications && notifyOnlyIfImportantChanged) {
            if (newNotifyOnlyIfImportant) {
                // User only wants to be notified for important news
                modifyAllUserGameSubscriptions(user, new TopicSubscriptionOperation[]{
                        new TopicSubscriptionOperation("regular", SubscriptionOperation.UNSUBSCRIBE)
                });
            } else {
                if (!receiveNotificationsChanged) {
                    // Only change this if the receive notitifcations setting has not been changed
                    // as it also subscribes to the regular topics
                    // User wants to be notified of everything
                    modifyAllUserGameSubscriptions(user, new TopicSubscriptionOperation[]{
                            new TopicSubscriptionOperation("regular", SubscriptionOperation.SUBSCRIBE)
                    });
                }
            }
        }
    }
}

/**
 * Represents an operation on a topic (with a specific suffix).
 */
class TopicSubscriptionOperation {
    private String topicSuffix;
    private NotificationService.SubscriptionOperation subscriptionOperation;

    public TopicSubscriptionOperation(String topicSuffix, NotificationService.SubscriptionOperation subscriptionOperation) {
        this.topicSuffix = topicSuffix;
        this.subscriptionOperation = subscriptionOperation;
    }

    public String getTopicSuffix() {
        return topicSuffix;
    }

    public void setTopicSuffix(String topicSuffix) {
        this.topicSuffix = topicSuffix;
    }

    public NotificationService.SubscriptionOperation getSubscriptionOperation() {
        return subscriptionOperation;
    }

    public void setSubscriptionOperation(NotificationService.SubscriptionOperation subscriptionOperation) {
        this.subscriptionOperation = subscriptionOperation;
    }
}