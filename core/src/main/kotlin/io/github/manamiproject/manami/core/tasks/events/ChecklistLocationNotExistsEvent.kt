package io.github.manamiproject.manami.core.tasks.events

import io.github.manamiproject.manami.entities.Anime

class ChecklistLocationNotExistsEvent(animeEntry: Anime) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.ERROR
        message = "Location does not exist."
    }
}