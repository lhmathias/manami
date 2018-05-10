package io.github.manamiproject.manami.core.events.checklist

import io.github.manamiproject.manami.core.events.checklist.AbstractChecklistEvent.ChecklistEventType.ERROR
import io.github.manamiproject.manami.entities.Anime

class LocationNotExistsChecklistEvent(
        animeEntry: Anime
) : AbstractChecklistEvent(animeEntry) {

    init {
        type = ERROR
        message = "Location does not exist."
    }
}