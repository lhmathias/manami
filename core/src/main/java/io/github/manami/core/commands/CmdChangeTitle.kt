package io.github.manami.core.commands

import io.github.manami.core.Manami
import io.github.manami.dto.entities.Anime


/**
 * Command for changing the title.
 *
 * @param anime Anime that is being edited.
 * @param newValue The new title.
 * @param application Instance of the application which reveals access to the persistence functionality.
 */
internal class CmdChangeTitle(
        private val anime: Anime,
        private val newValue: String,
        private val application: Manami
) : AbstractReversibleCommand(application) {

    init {
        oldAnime = anime
        newAnime = oldAnime?.copy(title = newValue)
    }
}
