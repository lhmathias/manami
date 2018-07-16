package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence


internal class CmdChangeLocation(
        private val anime: Anime,
        private val newValue: String,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    init {
        oldAnime = anime
        newAnime = oldAnime?.copy(location = newValue)
    }
}
