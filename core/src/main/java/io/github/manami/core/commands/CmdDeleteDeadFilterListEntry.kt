package io.github.manami.core.commands;

import io.github.manami.core.Manami
import io.github.manami.dto.entities.FilterEntry


class CmdDeleteDeadFilterListEntry(
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
