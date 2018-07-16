package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence


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
