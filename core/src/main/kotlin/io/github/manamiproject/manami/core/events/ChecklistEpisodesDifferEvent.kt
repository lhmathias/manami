package io.github.manamiproject.manami.core.events

import io.github.manamiproject.manami.entities.Anime

class ChecklistEpisodesDifferEvent(
        animeEntry: Anime,
        val newValue: Int
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The local number of episodes is [${animeEntry.episodes}] and infolink's number of episodes is [$newValue]"
    }
}
