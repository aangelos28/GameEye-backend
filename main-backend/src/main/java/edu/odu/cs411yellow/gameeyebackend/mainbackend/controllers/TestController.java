package edu.odu.cs411yellow.gameeyebackend.mainbackend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.ElasticGameCreationService;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final IgdbService igdbService;
    private final ElasticGameCreationService srv;

    @Autowired
    public TestController(IgdbService igdbService, ElasticGameCreationService srv) {
        this.igdbService = igdbService;
        this.srv = srv;
    }

    @GetMapping(path = "/public/test")
    public String getTest() {
        srv.createElasticGamesFromGames();
        return "";
    }

    @GetMapping(path = "/private/companies", produces = "application/json")
    public String getCompanies() throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(igdbService.getCompanies());
    }

    @GetMapping(path = "/private-admin/test")
    public String getAdminTest() {
        return "Private scoped admin test endpoint access success.";
    }
}
