package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.dto.entities.FilterListEntry
import io.github.manamiproject.manami.persistence.Persistence


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
