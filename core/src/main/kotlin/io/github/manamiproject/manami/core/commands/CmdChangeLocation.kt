package io.github.manamiproject.manami.core.commands

import io.github.manamiproject.manami.dto.entities.Anime
import io.github.manamiproject.manami.persistence.Persistence


/**
 * Command for changing the location.
 *
 * @param anime Anime to change.
 * @param newValue The new value.
 * @param persistence
 */
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
