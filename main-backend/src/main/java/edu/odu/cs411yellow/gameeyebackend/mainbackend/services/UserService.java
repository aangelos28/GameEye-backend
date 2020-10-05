package edu.odu.cs411yellow.gameeyebackend.mainbackend.services;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import  edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService
{
    @Autowired
    UserRepository userRepository;


    UserService (UserRepository otherRepository)
    {
        this.userRepository = otherRepository;
    }


    public void registerUser(String firebaseId)
    {
        User newbie = new User();
        newbie.setFirebaseId(firebaseId);
        userRepository.save(newbie);

    }

    public User findUserbyFirebase(String firebaseId)
    {
        return userRepository.findUserByFirebaseId(firebaseId);
    }
}
