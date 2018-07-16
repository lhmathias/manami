package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.persistence.Persistence


internal class CmdAddFilterEntry(
        private val entry: FilterListEntry,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return if(entry.isValid()) {
            persistence.filterAnime(entry)
        } else {
            false
        }
    }


    override fun undo() {
        if(entry.isValid()) {
            persistence.removeFromFilterList(entry)
        }
    }
}