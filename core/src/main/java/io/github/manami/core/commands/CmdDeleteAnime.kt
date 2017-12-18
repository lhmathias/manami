package io.github.manami.core.commands;

import io.github.manami.core.Manami;
import io.github.manami.dto.entities.Anime;

/**
 * Command for deleting an entry.
 * @param entry {@link Anime} that is supposed to be deleted.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
class CmdDeleteAnime(
        private val anime: Anime,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    init {
        oldAnime = anime
    }


    override fun execute(): Boolean {
        return app.removeAnime(oldAnime.id)
    }


    override fun undo() {
        app.addAnime(oldAnime)
    }
}
