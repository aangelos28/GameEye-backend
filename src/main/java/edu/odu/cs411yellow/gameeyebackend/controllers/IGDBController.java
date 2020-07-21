package edu.odu.cs411yellow.gameeyebackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.services.IgdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IGDBController {

    private IgdbService igdbService;

    @Autowired
    public IGDBController(IgdbService igdbService) {
        this.igdbService = igdbService;
    }

    @GetMapping(path = "/companies", produces = "application/json")
    public String getCompanies() {
        return igdbService.getCompanies();
    }
}
