package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.Content;
import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.Notifications;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;


public class Preferences {
    @Id
    private final String id;

    private Content content;

    private Notifications notifications;

    @PersistenceConstructor
    public Preferences(String id, Content content, Notifications notifications) {
        this.id = id;
        this.content = content;
        this.notifications = notifications;
    }

    public String getId() {
        return this.id;
    }

    public Content getContent() {
        return this.content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Notifications getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

}
