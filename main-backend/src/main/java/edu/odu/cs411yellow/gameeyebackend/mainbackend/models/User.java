package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a document in the "users" collection.
 */
@Document("users")
public class User {
    @Id
    private String id;

    private UserStatus status;

    private UserPlan plan;

    private Preferences preferences;

    private List<WatchedGame> watchList;

    @PersistenceConstructor
    public User(String id, UserStatus status, UserPlan plan, Preferences preferences, List<WatchedGame> watchList) throws Exception {
        if (id.equals("")) {
            throw new Exception("User id must not be null.");
        }

        this.id = id;
        this.status = status;
        this.plan = plan;
        this.preferences = preferences;
        this.watchList = watchList;
    }

    public User() {
        this.id = "";
        this.status = UserStatus.active;
        this.plan = UserPlan.free;
        this.preferences = new Preferences();
        this.watchList = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserPlan getPlan() {
        return this.plan;
    }

    public void setPlan(UserPlan plan) {
        this.plan = plan;
    }

    public Preferences getPreferences() {
        return this.preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public List<WatchedGame> getWatchList() {
        return this.watchList;
    }

    public void setWatchList(List<WatchedGame> watchList) {
        this.watchList = watchList;
    }

}
