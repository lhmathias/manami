package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.entities.AnimeType
import io.github.manamiproject.manami.entities.Anime

class TypesDifferChecklistEvent(
        animeEntry: Anime,
        val newType: AnimeType
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The local type is [${animeEntry.type}] and the infolink's type is [$newType]"
    }
}