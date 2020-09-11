package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.content;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.List;

public class BlockedContent {
    @Id
    private final String id;

    private List<String> resources;

    private List<String> newsWebsites;

    @PersistenceConstructor
    public BlockedContent(String id, List<String> resources, List<String> newsWebsites) {
        this.id = id;
        this.resources = resources;
        this.newsWebsites = newsWebsites;
    }

    public String getId() { return this.id; }

    public List<String> getResources() { return this.resources; }

    public void setResources(List<String> resources) { this.resources = resources; }

    public List<String> getNewsWebsites() {return this.newsWebsites; }

    public void setNewsWebsites(List<String> newsWebsites) { this.newsWebsites = newsWebsites; }

}
