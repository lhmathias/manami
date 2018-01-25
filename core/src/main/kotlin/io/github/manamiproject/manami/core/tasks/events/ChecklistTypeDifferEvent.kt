package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.dto.AnimeType
import io.github.manamiproject.manami.dto.entities.Anime

class ChecklistTypeDifferEvent(
        animeEntry: Anime,
        val newType: AnimeType
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The local type is [${animeEntry.type}] and the infolink's type is [$newType]"
    }
}
