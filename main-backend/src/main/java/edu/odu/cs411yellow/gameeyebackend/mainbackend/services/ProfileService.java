package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import  edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import org.slf4j.LoggerFactory;


@Service
public class ProfileService
{

    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    ProfileService (UserRepository otherRepository)
    {
        this.userRepository = otherRepository;
    }


    public void registerUser(final String firebaseId)
    {
        User newbie = new User();
        /*
        Newbie stays rrrreeeeeeee
         */

        newbie.setFirebaseId(firebaseId);
        userRepository.save(newbie);
    }

    public void deleteAccount(final String firebaseID){

        final User user = this.userRepository.findUserByFirebaseId(firebaseID);


        }

}
