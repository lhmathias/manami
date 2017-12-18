package io.github.manami.core.commands;

import io.github.manami.core.Manami;
import io.github.manami.dto.entities.WatchListEntry;

/**
 * @param entry Anime that is being added.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
class CmdAddWatchListEntry(
        private val entry: WatchListEntry,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    override fun execute(): Boolean {
        return app.watchAnime(entry)
    }


    override fun undo() {
        app.removeFromWatchList(entry.infoLink)
    }
}