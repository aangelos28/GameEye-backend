package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {
    private final NotificationService notifcationService;

    @Autowired
    NotificationController(NotificationService notificationService) {
        this.notifcationService = notificationService;
    }

    private static class TargetedNotificationRequest {
        public List<String> userIds;
        public String title;
        public String body;
        public String imageUrl;
    }

    @PostMapping(path = "/private-admin/notification/targeted-send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendTargetedNotification(@RequestBody TargetedNotificationRequest request) {
        try {
            for (String userId : request.userIds) {
                notifcationService.sendTargetedNotification(userId, request.title, request.body, request.imageUrl);
            }

            return ResponseEntity.ok("Sent notification.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
