package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.requests;

/**
 * Igdb controller request body class.
 * Used for admin endpoints in the igdb API.
 */
public class IgdbControllerRequest {
    private int minId;
    private int maxId;
    private int limit;

    public IgdbControllerRequest() {
    }

    public IgdbControllerRequest(int minId, int maxId, int limit) {
        this.minId = minId;
        this.maxId = maxId;
        this.limit = limit;
    }

    public int getMinId() {
        return minId;
    }

    public void setMinId(int minId) {
        this.minId = minId;
    }

    public int getMaxId() {
        return maxId;
    }

    public void setMaxId(int maxId) {
        this.maxId = maxId;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
