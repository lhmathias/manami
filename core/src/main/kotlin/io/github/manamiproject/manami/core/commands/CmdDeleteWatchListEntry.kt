package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence

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
        return if(anime.isValid()) {
            persistence.removeFromWatchList(anime)
        } else {
            false
        }
    }


    override fun undo() {
        if(anime.isValid() && !persistence.watchListEntryExists(anime.infoLink)) {
            persistence.watchAnime(anime)
        }
    }
}
