package io.github.manami.core.commands

import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.persistence.PersistenceHandler

/**
 * Adds an Anime to the filterlist.
 *
 * @param entry Anime that is being added.
 * @param persistence
 */
internal class CmdAddFilterEntry(
        private val entry: FilterListEntry,
        private val persistence: PersistenceHandler
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return persistence.filterAnime(entry)
    }


    override fun undo() {
        persistence.removeFromFilterList(entry)
    }
}