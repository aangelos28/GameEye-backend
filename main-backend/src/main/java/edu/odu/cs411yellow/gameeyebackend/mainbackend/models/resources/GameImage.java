package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;

public class GameImage {
    @Id
    private String id;

    private String title;

    private String imageId;

    private Date lastUpdated;

    @PersistenceConstructor
    public GameImage(String id, String title, String imageId, Date lastUpdated) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
        this.lastUpdated = lastUpdated;
    }

    public GameImage() {
        this.id = "";
        this.title = "";
        this.imageId = "";
        this.lastUpdated = new Date();
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getImageId () {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
