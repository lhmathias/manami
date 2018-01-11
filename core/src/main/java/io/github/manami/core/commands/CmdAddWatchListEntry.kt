package io.github.manami.core.commands

import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceHandler

/**
 * Adds an Anime to the watchlist.
 *
 * @param entry Anime that is being added.
 * @param persistence
 */
internal class CmdAddWatchListEntry(
        private val entry: WatchListEntry,
        private val persistence: PersistenceHandler
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return persistence.watchAnime(entry)
    }


    override fun undo() {
        persistence.removeFromWatchList(entry)
    }
}