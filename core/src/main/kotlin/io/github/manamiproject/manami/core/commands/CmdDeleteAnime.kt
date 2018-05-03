package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence

/**
 * Command for deleting an entry from animelist.
 *
 * @param anime {@link Anime} that is supposed to be deleted.
 * @param persistence
 */
internal class CmdDeleteAnime(
        private val anime: Anime,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    init {
        oldAnime = anime
    }


    override fun execute(): Boolean {
        oldAnime?.let { return persistence.removeAnime(it) }

        return false
    }


    override fun undo() {
        oldAnime?.let { persistence.addAnime(it) }
    }
}
