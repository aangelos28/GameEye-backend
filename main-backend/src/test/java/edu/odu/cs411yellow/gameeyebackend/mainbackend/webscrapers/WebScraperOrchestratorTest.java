package edu.odu.cs411yellow.gameeyebackend.mainbackend.webscrapers;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Game;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.Resources;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.ElasticGameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.elasticsearch.ElasticGame;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.GameRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories.NewsWebsiteRepository;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources.Article;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.GameService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ReferenceGameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WebScraperOrchestratorTest {

    private String[] scraperNames={"GameSpot","Eurogamer","PC Gamer", "IGN","GameEye Mock News"};

    @Autowired
    MockNewsScraper mock;

    List<Article> mockArticles;

    @Autowired
    UniversalScraper scraper;

    @Autowired
    WebScraperOrchestrator orchestrator;
    @Autowired
    WebScraperOrchestrator orchestratorMock;
    @Autowired
    NewsWebsiteRepository newsWebsiteRepository;

    @Autowired
    @Qualifier("elasticsearchOperations")
    ElasticsearchOperations elasticSearch;
    @Autowired
    private ElasticGameRepository elasticGames;

    //@Autowired
    //private ElasticGameRepositoryCustomImpl elastic;

    @Autowired
    private ReferenceGameService rgs;
    @Autowired
    private GameRepository games;

    @Autowired
    GameService gameService;

    private Game mockgame1;
    private Game mockgame2;
    private Game mockgame3;
    private Game mockgame4;

    //@Value("http://411Yellow.cpi.cs.odu.edu")
    //@Value("${ml.server.host}")
    private String serverHost;

    //@Value("7745")
    //@Value("${ml.server.port}")
    private Integer serverPort;

    //@Autowired
    //private MachineLearningService machine = new MachineLearningService(
    // @Value("${ml.server.host}") serverHost, @Value("${ml.server.port}") serverPort);

    Article og;

    @BeforeEach
    public void init(){

        Resources mock1Resources= new Resources();
        mockgame1 = new Game();
        mockgame1.setId("WebScraperOrchTest - MockGame1");
        mockgame1.setTitle("Halo Infinite");
        mockgame1.setResources(mock1Resources);

        Resources mock2Resources= new Resources();
        mockgame2 = new Game();
        mockgame2.setId("WebScraperOrchTest - MockGame2");
        mockgame2.setTitle("Cyberpunk 2077");
        mockgame2.setResources(mock2Resources);


        Resources mock3Resources= new Resources();
        mockgame3 = new Game();
        mockgame3.setId("WebScraperOrchTest - MockGame3");
        mockgame3.setTitle("Doom Eternal");
        mockgame3.setResources(mock3Resources);

        mock.scrape(mock.getScraperName());
        mockArticles=mock.getArticles();

        og = new Article(mockArticles.get(1)); //"Destiny 2: Beyond Light adds ice"
        List<Article> arts = new ArrayList<>(Arrays.asList(og));

        Resources mock4Resources= new Resources(null,arts);
        //Resources mock4Resources= new Resources();
        String title="Destiny 2: Beyond Light";
        mockgame4 = new Game();
        mockgame4.setId("WebScraperOrchTest - MockGame4");
        mockgame4.setTitle(title);
        mockgame4.setResources(mock4Resources);
        mock4Resources.setArticles(arts);
        mockgame4.setId("100000");
        //mockgame4.addArticleResources(og);


        games.save(mockgame1);
        games.save(mockgame2);
        games.save(mockgame3);
        games.save(mockgame4);

        ElasticGame elasticGame1 = new ElasticGame(mockgame1);
        ElasticGame elasticGame2 = new ElasticGame(mockgame2);
        ElasticGame elasticGame3 = new ElasticGame(mockgame3);
        ElasticGame elasticGame4 = new ElasticGame(mockgame4);

        elasticGames.save(elasticGame1);
        elasticGames.save(elasticGame2);
        elasticGames.save(elasticGame3);
        elasticGames.save(elasticGame4);

        orchestratorMock = new WebScraperOrchestrator(scraper, mock, elasticGames, rgs, newsWebsiteRepository,games, gameService);
    }

    @AfterEach
    public void deleteTestGames(){
        games.deleteById(mockgame1.getId());
        games.deleteById(mockgame2.getId());
        games.deleteById(mockgame3.getId());
        games.deleteById(mockgame4.getId());

        elasticGames.deleteByGameId(mockgame1.getId());
        elasticGames.deleteByGameId(mockgame2.getId());
        elasticGames.deleteByGameId(mockgame3.getId());
        elasticGames.deleteByGameId(mockgame4.getId());
    }

    @Test
    public void testInit(){

    }


    @Test
    public void testCheckArticleDuplicates(){
        String title = mockgame4.getTitle();
        String id = mockgame4.getId();

        Article dupe = new Article(og);
        Boolean duped = orchestratorMock.checkArticleDuplicates(id,dupe);

        assertEquals(og,dupe);
        assertThat(duped,is(true));

    }

    @Test
    public void testCheckIrrelevantArticles(){

        Article irrelevant = mockArticles.get(mockArticles.size()-1);    //"GameEye Launch"
        Article nonIrrelevant = mockArticles.get(0);    //"GameEye Launch"

        boolean irr = orchestratorMock.checkIrrelevantArticles(irrelevant);
        boolean nonIrr = orchestratorMock.checkIrrelevantArticles(nonIrrelevant);

        assertThat(irr,is(true));
        assertThat(nonIrr,is(false));

    }

    @Test
    public void testInsertDataIntoDatabase(){
        // Retrieve game before inserting articles
        Game preTestGame = games.findGameByTitle("Cyberpunk 2077");
        Resources preResources = preTestGame.getResources();
        List<Article> preArticles = preResources.getArticles();

        orchestratorMock.forceScrape(mock);
        System.out.println(orchestratorMock.toString());
        //List<Article> testArts = scrappyMock.getArticleCollection();
        orchestratorMock.insertArticlesIntoDatabase();

        // Retrieve game after inserting articles
        Game postTestGame = games.findGameByTitle("Cyberpunk 2077");
        Resources postResources = postTestGame.getResources();
        List<Article> postArticles = postResources.getArticles();

        assertNotEquals(preResources,postResources);
        assertNotEquals(preArticles,postArticles);

        assertNotEquals(preArticles.size(),postArticles.size());
    }

    @Test
    public void testRemoveFromCollection(){
        orchestratorMock.forceScrape(mock);
        List<Article> beforeRemoval = orchestratorMock.getArticleCollection();
        orchestratorMock.removeFromCollection(beforeRemoval.get(0));
        List<Article> afterRemoval = orchestratorMock.getArticleCollection();

        assertNotEquals(beforeRemoval,afterRemoval);
        assertNotEquals(beforeRemoval.size(),afterRemoval.size());
    }

    @Test
    public void testPerformAGRSForSingleGameMention(){

        orchestratorMock.forceScrape(mock);
        List<Article> articles = orchestratorMock.getArticleCollection();
        List<String> gameIDs = orchestratorMock.performArticleGameReferenceSearch(articles.get(0));
       //String game = gameIDs.get(0);
        System.out.println(articles.get(0).getTitle());
        for(String id:gameIDs){
            System.out.println("Game ID(s): "+id);
            System.out.println("Game: "+games.findGameByTitle(id));
        }

    }

    @Test
    public void testMockGameRepo(){
        GameRepository testRepo = orchestratorMock.games;
        for(Game game:testRepo.findAll()){
            System.out.println(game.getTitle());
        }
    }

    @Test
    public void testAddArticleToGameInTestRepo(){
        mock.scrape(mock.getScraperName());
        mockArticles=mock.getArticles();

       // Article og = new Article(mockArticles.get(1)); //"Destiny 2: Beyond Light adds ice"
        //mockgame4.addArticleResources(og);

        String artID = og.getId();
        String title = "Destiny 2: Beyond Light";
        String id = "5fa25fd86ffacd4ab297d3e1"; //Destiny 2
        //orchestratorMock.addArticleToGame(og,title);

        //Article a = orchestratorMock.games.findGameByTitle(title).getResources().findArticle(artID);
    }

    @Test
    public void testGetGamesArticles(){
        //mock.scrape(mock.getScrapperName());
        //mockArticles=mock.getArticles();
        String title = "Destiny 2: Beyond Light";

        //Article og = new Article(mockArticles.get(1)); //"Destiny 2: Beyond Light adds ice"
        //orchestratorMock.addArticleToGame(og,title);


        List <Article> target = orchestratorMock.games.findGameByTitle(title).getResources().getArticles();
        for(Article a: target){
            System.out.println(a.getTitle());
        }

    }
}


