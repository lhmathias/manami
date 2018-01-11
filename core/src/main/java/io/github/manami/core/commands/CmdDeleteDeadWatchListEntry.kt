package io.github.manami.core.commands

import io.github.manami.dto.entities.WatchListEntry
import io.github.manami.persistence.PersistenceHandler


internal class CmdDeleteDeadWatchListEntry(
        private val anime: WatchListEntry,
        private val persistence: PersistenceHandler
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return persistence.removeFromWatchList(anime)
    }


    override fun undo() {
        persistence.watchAnime(anime)
    }
}
