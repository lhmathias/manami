package io.github.manami.core.commands

import io.github.manami.dto.entities.FilterListEntry
import io.github.manami.persistence.Persistence

/**
 * Deletes an entry from filterlist.
 *
 * @param anime {@link FilterListEntry} that is supposed to be deleted.
 * @param persistence
 */
internal class CmdDeleteFilterEntry(
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
