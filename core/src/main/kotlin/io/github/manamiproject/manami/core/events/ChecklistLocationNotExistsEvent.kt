package io.github.manamiproject.manami.core.events

import io.github.manamiproject.manami.entities.Anime

class ChecklistLocationNotExistsEvent(animeEntry: Anime) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ChecklistEventType.ERROR
        message = "Location does not exist."
    }
}