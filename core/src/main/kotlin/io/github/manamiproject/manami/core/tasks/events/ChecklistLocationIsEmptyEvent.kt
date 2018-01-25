package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.dto.entities.Anime

class ChecklistLocationIsEmptyEvent(animeEntry: Anime) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.ERROR
        message = "Location is empty."
    }
}