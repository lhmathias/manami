package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.FilterListEntry


internal class CmdDeleteDeadFilterListEntry(
        private val anime: FilterListEntry,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    override fun execute(): Boolean {
        return app.removeFromFilterList(anime.infoLink)
    }


    override fun undo() {
        app.filterAnime(anime)
    }
}
