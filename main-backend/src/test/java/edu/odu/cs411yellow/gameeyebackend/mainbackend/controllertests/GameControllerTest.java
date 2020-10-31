package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllertests;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.GameController;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:application-test.properties")
@WebMvcTest(GameController.class)
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService service;

    @Test
    public void logoShouldReturnLogoUrlFromService() throws Exception {
        String gameId = "123456789";
        String endpoint = "/private/game/logo";
        String logoUrl = "//images.igdb.com/igdb/image/upload/t_thumb/co2h9s.jpg";

        when(service.getLogoUrl(gameId)).thenReturn(logoUrl);
        this.mockMvc.perform(post(endpoint))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(logoUrl)));
    }
}
