package io.github.manami.core.commands

import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.persistence.Persistence


internal class CmdDeleteDeadFilterListEntry(
        private val anime: FilterListEntry,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return persistence.removeFromFilterList(anime)
    }


    override fun undo() {
        persistence.filterAnime(anime)
    }
}
