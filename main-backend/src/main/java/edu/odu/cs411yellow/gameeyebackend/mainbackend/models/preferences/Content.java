package edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.models.preferences.content.BlockedContent;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

public class Content {
    @Id
    private final String id;

    private Boolean showArchivedResources;

    private BlockedContent blockedContent;

    @PersistenceConstructor
    public Content(String id, Boolean showArchivedResources, BlockedContent blockedContent) {
        this.id = id;
        this.showArchivedResources = showArchivedResources;
        this.blockedContent = blockedContent;
    }

    public String getId() {
        return this.id;
    }

    public Boolean getShowArchivedResources() {
        return this.showArchivedResources;
    }

    public void setShowArchivedResources(Boolean showArchivedResources) {
        this.showArchivedResources = showArchivedResources;
    }

    public BlockedContent getBlockedContent() {
        return this.blockedContent;
    }

    public void setBlockedContent(BlockedContent blockedContent) {
        this.blockedContent = blockedContent;
    }

}
