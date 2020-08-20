package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("images")
public class Image {
    @Id
    private String id;

    private String type;

    private Binary data;

    @PersistenceConstructor
    public Image(String id, String type, Binary data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    ///////////////////////////////////////////////
    // Getters/Setters
    ///////////////////////////////////////////////
    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Binary getData() {
        return this.data;
    }

    public void setData(Binary data) {
        this.data = data;
    }
}
