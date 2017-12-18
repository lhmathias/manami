package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.Anime


/**
 * Command for changing the location.
 * @param anime Anime to change.
 * @param newValue The new value.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
class CmdChangeLocation(
        private val anime: Anime,
        private val newValue: String,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    init {
        oldAnime = anime
        newAnime = oldAnime.copy(location = newValue)
    }
}
