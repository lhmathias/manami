package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.WatchListEntry

/**
 * Deletes an entry from watchlist.
 *
 * @param anime {@link WatchListEntry} that is supposed to be deleted.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
internal class CmdDeleteWatchListEntry(
        private val anime: WatchListEntry,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    override fun execute(): Boolean {
        return app.removeFromWatchList(anime.infoLink)
    }


    override fun undo() {
        app.watchAnime(anime)
    }
}
