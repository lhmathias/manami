package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.dto.entities.Anime

class ChecklistEpisodesDifferEvent(
        animeEntry: Anime,
        val newValue: Int
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The local number of episodes is [${animeEntry.episodes}] and infolink's number of episodes is [$newValue]"
    }
}
