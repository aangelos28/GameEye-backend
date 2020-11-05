package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllertests;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.GameController;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.UserController;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.UserRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.UserController.SettingsRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations="classpath:application-test.properties")
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    private WebClient webClient;

    /*
    @Test
    public void testSettingsEndpoint() throws Exception {
        String endpoint = "/private/settings/update";
        ObjectMapper mapper = new ObjectMapper();

         boolean showArticles;
         boolean showImages;
         boolean notifyOnlyIfImportant;

        showArticles = true;
        showImages = false;
        notifyOnlyIfImportant = false;

        String validGameId = "123456789";
        validRequest.id = validGameId;

        // Test for ok response upon valid upadate
        SettingsRequest validRequest = new SettingsRequest(showArticles, showImages, notifyOnlyIfImportant);

        when(userService.updateSettings()  .thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(validRequest))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        // Test for not found reply upon invalid game title

    }
    */

}

