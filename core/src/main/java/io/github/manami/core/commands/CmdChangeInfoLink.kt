package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.Anime
import io.github.manami.dto.entities.InfoLink


/**
 * Command for changing the info link.
 * @param anime Anime to change.
 * @param newValue The new value.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
class CmdChangeInfoLink(
        private val anime: Anime,
        private val newValue: InfoLink,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    init {
        oldAnime = anime
        newAnime = oldAnime.copy(infoLink = newValue)
    }
}
