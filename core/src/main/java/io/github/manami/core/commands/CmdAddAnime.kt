package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.Anime

/**
 * Command for adding an entry.
 * @param anime Anime that is being added.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
internal class CmdAddAnime(
        private val anime: Anime,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    override fun execute(): Boolean {
        return application.addAnime(anime)
    }


    override fun undo() {
        application.removeAnime(anime.id)
    }
}