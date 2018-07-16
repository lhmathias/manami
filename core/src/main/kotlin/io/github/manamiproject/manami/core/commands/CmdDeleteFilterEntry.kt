package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.persistence.Persistence


internal class CmdDeleteFilterEntry(
        private val anime: FilterListEntry,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return if(anime.isValid()) {
            persistence.removeFromFilterList(anime)
        } else {
            false
        }
    }


    override fun undo() {
        if(anime.isValid()  && !persistence.filterListEntryExists(anime.infoLink)) {
            persistence.filterAnime(anime)
        }
    }
}
