package edu.odu.cs411yellow.gameeyebackend.mainbackend.models;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class representing a document in the "Images" collection.
 */
@Document("images")
public class Image {
    @Id
    private final String id;

    /**
     * The type of the image (i.e. logo or thumbnail).
     */
    private String type;

    /**
     * Image binary data.
     */
    private Binary data;

    @PersistenceConstructor
    public Image(String id, String type, Binary data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public Image() {
        this.id = "";
        this.type = "";
        this.data = new Binary(new byte[1]);
    }

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
