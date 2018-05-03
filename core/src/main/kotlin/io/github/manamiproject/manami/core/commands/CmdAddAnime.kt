package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence

/**
 * Command for adding an entry.
 *
 * @param anime Anime that is being added.
 * @param persistence
 */
internal class CmdAddAnime(
        private val anime: Anime,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return if(anime.isValid()) {
            persistence.addAnime(anime)
        } else {
            false
        }
    }


    override fun undo() {
        if(anime.isValid()) {
            persistence.removeAnime(anime)
        }
    }
}