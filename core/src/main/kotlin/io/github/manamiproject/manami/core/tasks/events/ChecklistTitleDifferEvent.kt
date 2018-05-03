package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.entities.Anime

class ChecklistTitleDifferEvent(
        animeEntry: Anime,
        val newTitle: String
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.WARNING
        message = "The local title is [${animeEntry.title}] and the infolink's title is [$newTitle]"
    }
}
