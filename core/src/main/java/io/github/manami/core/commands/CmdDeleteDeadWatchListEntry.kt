package io.github.manami.core.commands;

import io.github.manami.core.Manami
import io.github.manami.dto.entities.WatchListEntry


class CmdDeleteDeadWatchListEntry(
        private val anime: WatchListEntry,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    override fun execute(): Boolean {
        return app.removeFromWatchList(anime.infoLink)
    }


    override fun undo() {
        app.watchAnime(anime)
    }
}
