package io.github.manami.core.commands

import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.persistence.PersistenceHandler


internal class CmdDeleteDeadFilterListEntry(
        private val anime: FilterListEntry,
        private val persistence: PersistenceHandler
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return persistence.removeFromFilterList(anime)
    }


    override fun undo() {
        persistence.filterAnime(anime)
    }
}
