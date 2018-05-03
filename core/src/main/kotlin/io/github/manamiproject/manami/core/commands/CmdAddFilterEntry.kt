package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.FilterListEntry
import io.github.manamiproject.manami.persistence.Persistence

/**
 * Adds an Anime to the filterlist.
 *
 * @param entry Anime that is being added.
 * @param persistence
 */
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