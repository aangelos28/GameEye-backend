package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllertests;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.GameController.LogoRequest;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.GameController.ArticlesRequest;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.GameController.GameTitleAutocompletionRequest;
import static edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers.GameController.TopGamesRequest;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations="classpath:application-test.properties")
public class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Autowired
    private ElasticGameRepository elasticGames;

    private WebClient webClient;

    @Test
    public void completeShouldReturnTitlesFromElasticsearch() throws Exception {
        String endpoint = "/private/game/complete";
        ObjectMapper mapper = new ObjectMapper();

        // Test for ok response upon valid game title
        GameTitleAutocompletionRequest validRequest = new GameTitleAutocompletionRequest();
        String validTitle = "Doom Eternal";
        validRequest.gameTitle = validTitle;

        this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(validRequest))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        // Test for not found reply upon invalid game title
        GameTitleAutocompletionRequest invalidRequest = new GameTitleAutocompletionRequest();
        String invalidTitle = "";
        validRequest.gameTitle = validTitle;

        invalidRequest.gameTitle = invalidTitle;

        this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(invalidRequest))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void logoShouldReturnLogoUrlFromService() throws Exception {
        String endpoint = "/private/game/logo";
        String logoUrl = "//images.igdb.com/igdb/image/upload/t_thumb/co2h9s.jpg";
        String errorMessage = "Game with specified id not found.";
        ObjectMapper mapper = new ObjectMapper();

        // Test for logo response upon valid game id
        LogoRequest validRequest = new LogoRequest();
        String validGameId = "123456789";
        validRequest.id = validGameId;

        when(gameService.existsById(validGameId)).thenReturn(true);
        when(gameService.getLogoUrl(validGameId)).thenReturn(logoUrl);

        this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(validRequest))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(logoUrl));

        // Test for not found response upon invalid game id
        LogoRequest invalidRequest = new LogoRequest();
        String invalidGameId = "";
        invalidRequest.id = invalidGameId;

        when(gameService.existsById(invalidGameId)).thenReturn(false);

        invalidRequest.id = invalidGameId;

        this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(invalidRequest))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    public void articlesShouldReturnArticlesFromService() throws Exception {
        String endpoint = "/private/game/articles";
        String errorMessage = "Game with specified id not found.";
        ObjectMapper mapper = new ObjectMapper();

        // Test for articles response upon valid game id
        ArticlesRequest validRequest = new ArticlesRequest();
        String validGameId = "123456789";
        validRequest.id = validGameId;

        Article article = new Article();
        String title = "Zelda BOTW Release Date";
        article.setTitle(title);
        List<Article> articles = new ArrayList<>();
        articles.add(article);

        when(gameService.existsById(validGameId)).thenReturn(true);
        when(gameService.getArticles(validGameId)).thenReturn(articles);

        this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(validRequest))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        // Test for not found response upon invalid game id
        ArticlesRequest invalidRequest = new ArticlesRequest();
        String invalidGameId = "";
        invalidRequest.id = invalidGameId;

        when(gameService.existsById(invalidGameId)).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(invalidRequest))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    public void topShouldReturnTopGamesFromService() throws Exception {
        String endpoint = "/private/game/top";
        ObjectMapper mapper = new ObjectMapper();

        // Test for top games response upon valid max results
        TopGamesRequest validRequest = new TopGamesRequest();
        int validMaxResults= 2;
        validRequest.maxResults = validMaxResults;

        Game game1 = new Game();
        Game game2 = new Game();
        Game game3 = new Game ();

        game1.setTitle("Doom");
        game1.setWatchers(1);

        game2.setTitle("Zelda");
        game2.setWatchers(10);

        game3.setTitle("Warzone");
        game3.setWatchers(50);

        List<Game> games = new ArrayList<>(Arrays.asList(game1, game2, game3));

        when(gameService.getTopGames(validMaxResults)).thenReturn(games);

        this.mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(validRequest))
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}
