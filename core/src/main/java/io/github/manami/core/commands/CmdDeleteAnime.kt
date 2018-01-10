package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.Anime

/**
 * Command for deleting an entry from animelist.
 *
 * @param anime {@link Anime} that is supposed to be deleted.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
internal class CmdDeleteAnime(
        private val anime: Anime,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    init {
        oldAnime = anime
    }


    override fun execute(): Boolean {
        oldAnime?.let { anime ->
            return app.removeAnime(anime.id)
        }

        return false
    }


    override fun undo() {
        oldAnime?.let { anime ->
            app.addAnime(anime)
        }
    }
}
