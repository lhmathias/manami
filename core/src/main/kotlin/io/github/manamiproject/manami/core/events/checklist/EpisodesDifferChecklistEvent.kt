package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType.WARNING
import io.github.manamiproject.manami.entities.Anime

class EpisodesDifferChecklistEvent(
        animeEntry: Anime,
        newValue: Int
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = WARNING
        message = "The local number of episodes is [${animeEntry.episodes}] and infolink's number of episodes is [$newValue]"
    }
}
