package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.Anime

/**
 * Command for changing the value of an episode.
 *
 * @param anime Anime that is being edited.
 * @param newValue The new title.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
internal class CmdChangeEpisodes(
        private val anime: Anime,
        private val newValue: Int,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    init {
        oldAnime = anime
        newAnime = oldAnime?.copy(numberOfEpisodes = newValue)
    }
}
