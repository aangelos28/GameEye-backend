package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.resources;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.SourceUrls;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Date;
import java.util.Objects;

public class ImageResource {
    @BsonProperty("id")
    private String id;

    private String title;

    private String imageId;

    private Date lastUpdated;

    @PersistenceConstructor
    public ImageResource(String id, String title, String imageId, Date lastUpdated) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
        this.lastUpdated = lastUpdated;
    }

    public ImageResource() {
        this.id = "";
        this.title = "";
        this.imageId = "";
        this.lastUpdated = new Date();
    }

    public String getId () {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o ) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageResource that = (ImageResource) o;

        return Objects.equals(id, that.id)
                && Objects.equals(imageId, that.imageId)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageId, title);
    }
}
