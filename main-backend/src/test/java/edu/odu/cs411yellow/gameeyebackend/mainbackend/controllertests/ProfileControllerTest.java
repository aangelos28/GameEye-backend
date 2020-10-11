package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllertests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.ProfileController;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.User;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ProfileService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {

    @InjectMocks ProfileController profileController;

    @Mock ProfileService profileService;

    /*
    @Test
    public void AddNewProfile(){

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));




    }
    */

}
