package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence


internal class CmdChangeType(
        private val anime: Anime,
        private val newValue: AnimeType,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    init {
        oldAnime = anime
        newAnime = oldAnime?.copy(type = newValue)
    }
}
