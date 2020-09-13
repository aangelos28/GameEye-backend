package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Class representing a document in the "users" collection.
 */
@Document("users")
public class User {
    @Id
    private final String id;

    private String firstName;

    private String lastName;

    private String email;

    private String status;

    private String plan;

    private ContentPreferences contentPreferences;

    private List<WatchedGame> watchList;

    @PersistenceConstructor
    public User(String id, String firstName, String lastName, String email, String status,
                String plan, ContentPreferences contentPreferences, List<WatchedGame> watchList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.plan = plan;
        this.contentPreferences = contentPreferences;
        this.watchList = watchList;
    }

    public String getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlan() {
        return this.plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public ContentPreferences getPreferences() {
        return this.contentPreferences;
    }

    public void setPreferences(ContentPreferences contentPreferences) {
        this.contentPreferences = contentPreferences;
    }

    public List<WatchedGame> getWatchList() {
        return this.watchList;
    }

    public void setWatchList(List<WatchedGame> watchList) {
        this.watchList = watchList;
    }

}