package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.FilterListEntry

/**
 * @param entry Anime that is being added.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
internal class CmdAddFilterEntry(
        private val entry: FilterListEntry,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    override fun execute(): Boolean {
        return app.filterAnime(entry)
    }


    override fun undo() {
        app.removeFromFilterList(entry.infoLink)
    }
}