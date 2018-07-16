package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.WatchListEntry
import io.github.manamiproject.manami.persistence.Persistence


internal class CmdAddWatchListEntry(
        private val entry: WatchListEntry,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return if(entry.isValid()) {
            persistence.watchAnime(entry)
        } else {
            false
        }
    }


    override fun undo() {
        if(entry.isValid()) {
            persistence.removeFromWatchList(entry)
        }
    }
}