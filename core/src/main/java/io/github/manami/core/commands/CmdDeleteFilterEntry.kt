package io.github.manami.core.commands;

import io.github.manami.core.Manami
import io.github.manami.dto.entities.FilterEntry

/**
 * @param entry {@link FilterEntry} that is supposed to be deleted.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
class CmdDeleteFilterEntry(
        private val anime: FilterEntry,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    override fun execute(): Boolean {
        return app.removeFromFilterList(anime.infoLink)
    }


    override fun undo() {
        app.filterAnime(anime)
    }
}
