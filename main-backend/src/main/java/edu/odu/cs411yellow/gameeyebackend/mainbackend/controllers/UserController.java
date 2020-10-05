
package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Preferences;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.UserPlan;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.UserStatus;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.WatchedGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.UserService;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService aService)
    {
        super();
        this.userService = aService;
    }

    public void addNewUser(String firebaseId)
    {
        userService.registerUser(firebaseId);
    }


    /*  To be implemented later when we start creating lists of users.
    public String showAllUsers()
    {

    }

*/

}
