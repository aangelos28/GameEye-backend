
package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;
import com.google.firebase.auth.FirebaseToken;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.*;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ProfileService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;



@RestController
public class ProfileController {

    private ProfileService profileService;
    private UserRepository scanner;

    Logger logger;
    @Autowired
    public ProfileController(ProfileService aService)
    {
        super();
        this.profileService = aService;
    }



@PostMapping(path = "/profile/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewProfile()
    {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final FirebaseToken fbToken = (FirebaseToken) auth.getPrincipal();
        final String firebaseID = fbToken.getUid();

        try {
            User found = scanner.findUserByFirebaseId(firebaseID);
            final String response = String.format("User has already been registered in mongo DB, cannot register an existing user");
            logger.info(response);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception ex)
        {
            profileService.registerUser(firebaseID);
            ex.printStackTrace();
            final String response = String.format("User not found, but is now registered in mongo DB");
            logger.info(response);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }



}
