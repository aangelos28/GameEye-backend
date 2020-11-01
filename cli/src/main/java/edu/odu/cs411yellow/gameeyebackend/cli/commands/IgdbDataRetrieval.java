package edu.odu.cs411yellow.gameeyebackend.cli.commands;

import edu.odu.cs411yellow.gameeyebackend.mainbackend.services.IgdbReplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Contains CLI commands related to IGDB data retrieval.
 */
@ShellComponent
public class IgdbDataRetrieval {
    private IgdbReplicationService service;

    @Autowired
    public IgdbDataRetrieval(IgdbReplicationService service) {
        this.service = service;
    }

    /**
     * Replicates IGDB game data to GameEye database.
     *
     * @param minId
     * @param maxId
     * @param limit
     * @return User ID.
     */
    @ShellMethod(value = "Replicate IGDB game data.", key = "replicate-igdb")
    public void replicateIgdb(@ShellOption("--min-id") int minId,
                              @ShellOption("--max-id") int maxId,
                              @ShellOption("--limit")  int limit) {

        String result = service.replicateIgdbByRange(minId, maxId, limit);

        System.out.printf(result);
    }
}
