package io.github.manami.core.commands;

import io.github.manami.core.Manami;
import io.github.manami.dto.entities.FilterEntry;

/**
 * @param entry Anime that is being added.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
class CmdAddFilterEntry(
        private val entry: FilterEntry,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    override fun execute(): Boolean {
        return app.filterAnime(entry)
    }


    override fun undo() {
        app.removeFromFilterList(entry.infoLink)
    }
}