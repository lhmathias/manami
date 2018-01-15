package io.github.manami.core.commands

import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.InfoLink
import io.github.manami.persistence.Persistence


/**
 * Command for changing the info link.
 *
 * @param anime Anime to change.
 * @param newValue The new value.
 * @param persistence
 */
internal class CmdChangeInfoLink(
        private val anime: Anime,
        private val newValue: InfoLink,
        private val persistence: Persistence
) : AbstractReversibleCommand(persistence) {

    init {
        oldAnime = anime
        newAnime = oldAnime?.copy(infoLink = newValue)
    }
}
