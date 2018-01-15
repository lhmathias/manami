package io.github.manami.core.commands

import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.Persistence

/**
 * Deletes an entry from watchlist.
 *
 * @param anime {@link WatchListEntry} that is supposed to be deleted.
 * @param persistence Instance of the persistence which reveals access to the persistence functionality.
 */
internal class CmdDeleteWatchListEntry(
        private val anime: WatchListEntry,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return persistence.removeFromWatchList(anime)
    }


    override fun undo() {
        persistence.watchAnime(anime)
    }
}
