package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence


internal class CmdDeleteAnime(
        private val anime: Anime,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    override fun execute(): Boolean {
        return if(anime.isValid()) {
            persistence.removeAnime(anime)
        } else {
            false
        }
    }


    override fun undo() {
        if(anime.isValid() && !persistence.animeEntryExists(anime.infoLink)) {
            persistence.addAnime(anime)
        }
    }
}
