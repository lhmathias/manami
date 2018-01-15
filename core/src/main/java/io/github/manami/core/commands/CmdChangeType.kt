package io.github.manami.core.commands

import io.github.manami.dto.AnimeType
import io.github.manami.dto.entities.Anime
import io.github.manami.persistence.Persistence

/**
 * Command for changing the type.
 *
 * @param anime Anime to change
 * @param newValue The new value.
 * @param persistence
 */
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
