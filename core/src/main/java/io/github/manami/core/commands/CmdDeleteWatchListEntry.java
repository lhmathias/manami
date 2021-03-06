package io.github.manami.core.commands;

import io.github.manami.core.Manami;
import io.github.manami.dto.entities.WatchListEntry;

/**
 * @author manami-project
 * @since 2.8.0
 */
public class CmdDeleteWatchListEntry extends AbstractReversibleCommand {

    private final WatchListEntry entry;


    /**
     * Constructor
     *
     * @since 2.7.0
     * @param entry {@link WatchListEntry} that is supposed to be deleted.
     * @param application Instance of the application which reveals access to the persistence functionality.
     */
    public CmdDeleteWatchListEntry(final WatchListEntry entry, final Manami application) {
        this.entry = entry;
        app = application;
    }


    @Override
    public boolean execute() {
        return app.removeFromWatchList(entry.getInfoLink());
    }


    @Override
    public void undo() {
        app.watchAnime(entry);
    }
}
