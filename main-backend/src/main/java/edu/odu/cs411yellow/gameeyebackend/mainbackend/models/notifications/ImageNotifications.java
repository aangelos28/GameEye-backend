package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.notifications;

import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;

public class ImageNotifications {
    private List<String> imageIds;

    @PersistenceConstructor
    public ImageNotifications(List<String> imageIds) {
        this.imageIds = imageIds;
    }

    public ImageNotifications() {
        this.imageIds = new ArrayList<>();
    }

    public List<String> getImageIds() {
        return this.imageIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }
}
