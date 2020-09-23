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
    private final String id;

    private String firstName;

    private String lastName;

    private String email;

    private UserStatus status;

    private UserPlan plan;

    private Preferences preferences;

    private List<WatchedGame> watchList;

    @PersistenceConstructor
    public User(String id, String firstName, String lastName, String email, UserStatus status,
                UserPlan plan, Preferences preferences, List<WatchedGame> watchList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.plan = plan;
        this.preferences = preferences;
        this.watchList = watchList;
    }

    public User() {
        this.id = "";
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.status = UserStatus.active;
        this.plan = UserPlan.free;
        this.preferences = new Preferences();
        this.watchList = new ArrayList<>();
    }

    public User(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.status = user.status;
        this.plan = user.plan;
        this.preferences = user.preferences;
        this.watchList = user.watchList;
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