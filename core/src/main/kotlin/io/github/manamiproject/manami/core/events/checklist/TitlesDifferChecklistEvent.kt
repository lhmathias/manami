package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.entities.Anime

class TitlesDifferChecklistEvent(
       animeEntry: Anime,
       newTitle: String
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The local title is [${animeEntry.title}] and the infolink's title is [$newTitle]"
    }
}
