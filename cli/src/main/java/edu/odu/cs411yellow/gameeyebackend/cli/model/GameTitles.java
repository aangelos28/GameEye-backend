package edu.odu.cs411yellow.gameeyebackend.cli.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
public class GameTitles {
    public List<String> titles;

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

}
