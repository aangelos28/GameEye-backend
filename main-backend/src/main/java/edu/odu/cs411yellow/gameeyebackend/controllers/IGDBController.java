package edu.odu.cs411yellow.gameeyebackend.controllers;

import edu.odu.cs411yellow.gameeyebackend.services.IgdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/private")
public class IGDBController {

    private final IgdbService igdbService;

    @Autowired
    public IGDBController(IgdbService igdbService) {
        this.igdbService = igdbService;
    }

    @GetMapping(path = "/public/test")
    public String getTest() {
        return "Public endpoint access success.";
    }

    @GetMapping(path = "/private/companies", produces = "application/json")
    public String getCompanies() {
        return igdbService.getCompanies();
    }

    @GetMapping(path = "/private-admin/test")
    public String getAdminTest() {
        return "Private scoped admin test endpoint access success.";
    }
}
